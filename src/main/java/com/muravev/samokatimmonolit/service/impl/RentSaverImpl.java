package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.*;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.event.InventoryStatusChangedEvent;
import com.muravev.samokatimmonolit.model.InventoryStatus;
import com.muravev.samokatimmonolit.model.RentStatus;
import com.muravev.samokatimmonolit.model.in.command.rent.RentCreateCommand;
import com.muravev.samokatimmonolit.model.out.PaymentOptionsOut;
import com.muravev.samokatimmonolit.repo.InventoryMonitoringRepo;
import com.muravev.samokatimmonolit.repo.InventoryRepo;
import com.muravev.samokatimmonolit.repo.RentRepo;
import com.muravev.samokatimmonolit.repo.TariffRepo;
import com.muravev.samokatimmonolit.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.TreeSet;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentSaverImpl implements RentSaver {
    private final RentRepo rentRepo;
    private final TariffRepo tariffRepo;

    private final InventoryMonitoringRepo monitoringRepo;

    private final InventorySaver inventorySaver;
    private final InventoryReader inventoryReader;

    private final SecurityService securityService;
    private final ApplicationEventPublisher eventPublisher;
    private final PaymentService paymentService;


    @Override
    @Transactional
    public PaymentOptionsOut start(RentCreateCommand command) {
        ClientEntity currentClient = securityService.getCurrentClient();

        InventoryEntity inventory = inventoryReader.findByIdAsClient(command.inventoryId());

        OrganizationTariffEntity tariff = tariffRepo.findById(command.tariffId())
                .filter(not(OrganizationTariffEntity::isDeleted))
                .orElseThrow(() -> new ApiException(StatusCode.TARIFF_NOT_FOUND));

        RentEntity rent = new RentEntity()
                .setClient(currentClient)
                .setInventory(inventory)
                .setStatus(RentStatus.ACTIVE)
                .setTariff(tariff);

        inventorySaver.changeStatus(inventory, InventoryStatus.IN_WORK);

        RentEntity savedRent = rentRepo.save(rent);
        return paymentService.deposit(savedRent);
    }

    @Override
    @Transactional
    public PaymentOptionsOut end(long id) {
        ClientEntity currentClient = securityService.getCurrentClient();
        RentEntity rent = rentRepo.findByIdAndClient(id, currentClient)
                .orElseThrow(() -> new ApiException(StatusCode.RENT_NOT_FOUND));
        rent.setEndTime(ZonedDateTime.now());
        InventoryEntity inventory = rent.getInventory();
        fixationTrack(rent);
        inventorySaver.changeStatus(inventory, InventoryStatus.PENDING);
        eventPublisher.publishEvent(InventoryStatusChangedEvent.of(inventory, InventoryStatus.PENDING));
        return paymentService.pay(rent);
    }

    private void fixationTrack(RentEntity rent) {
        InventoryEntity inventory = rent.getInventory();
        List<InventoryMonitoringEntity> track =
                monitoringRepo.findAllByInventoryAndTime(
                        inventory,
                        rent.getStartTime(),
                        rent.getEndTime());
        TreeSet<InventoryMonitoringEntity> filteredTrack = new TreeSet<>(track.stream()
                .filter(x -> ObjectUtils.notEqual(x.getSatellites(), 0))
                .toList());
        rent.setTrack(filteredTrack);
    }

    @Scheduled(fixedDelay = 30000L)
    @Transactional
    public void autoCancelStartingRent() {
        List<RentEntity> rents = rentRepo.findAllStartingRents(ZonedDateTime.now().minusMinutes(5));
        rents.forEach(this::cancelRent);
    }

    private void cancelRent(RentEntity rent) {
        inventorySaver.changeStatus(rent.getInventory(), InventoryStatus.PENDING);
        rent.setStatus(RentStatus.CANCELED);
    }
}
