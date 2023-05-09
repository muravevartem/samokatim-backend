package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmessage.GeoPointReceivedMessage;
import com.muravev.samokatimmonolit.entity.*;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.event.InventoryStatusChangedEvent;
import com.muravev.samokatimmonolit.model.InventoryStatus;
import com.muravev.samokatimmonolit.model.in.InventoryModelIn;
import com.muravev.samokatimmonolit.model.in.command.inventory.*;
import com.muravev.samokatimmonolit.repo.InventoryModelRepo;
import com.muravev.samokatimmonolit.repo.InventoryRepo;
import com.muravev.samokatimmonolit.repo.TariffRepo;
import com.muravev.samokatimmonolit.service.InventorySaver;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventorySaverImpl implements InventorySaver {
    private final InventoryRepo inventoryRepo;
    private final InventoryModelRepo inventoryModelRepo;
    private final TariffRepo tariffRepo;

    private final SecurityService securityService;

    private final ApplicationEventPublisher eventPublisher;


    @Override
    @Transactional
    public InventoryEntity create(InventoryCreateCommand createCommand) {
        EmployeeEntity currentEmployee = securityService.getCurrentEmployee();
        OrganizationEntity organization = currentEmployee.getOrganization();

        InventoryModelIn model = createCommand.model();


        InventoryEntity inventory = new InventoryEntity();
        inventory.setInventoryClass(createCommand.inventoryClass());
        inventory.setModel(inventoryModelRepo.getReferenceById(model.id()));
        inventory.setOrganization(organization);
        inventory.setAlias(createCommand.alias());
        inventory.setSupportsTelemetry(createCommand.supportsTelemetry());
        inventory.setStatus(InventoryStatus.UNDER_REPAIR);//По умолчанию на старте числиться в ремонте
        return inventoryRepo.save(inventory);
    }

    @Override
    @Transactional
    public InventoryEntity changeField(long id, InventoryChangeStatusCommand command) {
        InventoryEntity inventory = getOneAsEmployee(id);
        inventory.setStatus(command.status());
        eventPublisher.publishEvent(InventoryStatusChangedEvent.of(inventory, command.status()));
        return inventory;
    }

    @Override
    @Transactional
    public InventoryEntity changeField(long id, InventoryChangeAliasCommand command) {
        InventoryEntity inventory = getOneAsEmployee(id);
        inventory.setAlias(command.alias());
        return inventory;
    }

    @Override
    @Transactional
    public InventoryEntity changeField(long id, InventoryChangeModelCommand command) {
        InventoryEntity inventory = getOneAsEmployee(id);
        InventoryModelIn model = command.model();
        inventory.setModel(inventoryModelRepo.getReferenceById(model.id()));
        return inventory;
    }

    @Override
    @Transactional
    public InventoryEntity changeField(long id, InventoryChangeClassCommand command) {
        InventoryEntity inventory = getOneAsEmployee(id);
        inventory.setInventoryClass(command.inventoryClass());
        return inventory;
    }

    @Override
    @Transactional
    public InventoryEntity changeField(long id, InventoryAddTariffCommand command) {
        InventoryEntity inventory = getOneAsEmployee(id);
        EmployeeEntity employee = securityService.getCurrentEmployee();
        OrganizationTariffEntity tariff = tariffRepo.findById(command.tariffId())
                .filter(t -> Objects.equals(employee.getOrganization(), t.getOrganization()))
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new ApiException(StatusCode.TARIFF_NOT_FOUND));
        inventory.getTariffs().add(tariff);
        return inventory;
    }

    @Override
    @Transactional
    public InventoryEntity changeField(long id, InventoryDeleteTariffCommand command) {
        InventoryEntity inventory = getOneAsEmployee(id);
        inventory.getTariffs().remove(tariffRepo.getReferenceById(command.tariffId()));
        return inventory;
    }

    private InventoryEntity getOneAsEmployee(long id) {
        EmployeeEntity employee = securityService.getCurrentEmployee();
        return inventoryRepo.findById(id)
                .filter(i -> Objects.equals(i.getOrganization(), employee.getOrganization()))
                .orElseThrow(() -> new ApiException(StatusCode.INVENTORY_NOT_FOUND));
    }

    @Override
    @Transactional
    public void delete(long id) {
        inventoryRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void savePoint(GeoPointReceivedMessage message) {
        Long inventoryId = message.getInventoryId();
        log.info("New geo point for inventory {}", inventoryId);
        InventoryEntity inventory = inventoryRepo.findById(inventoryId)
                .orElseThrow(() -> new ApiException(StatusCode.INVENTORY_NOT_FOUND));
        List<InventoryMonitoringEntity> monitoringRecord = inventory.getMonitoringRecord();
        ZonedDateTime timestamp = ZonedDateTime.ofInstant(Instant.ofEpochSecond(message.getTimestamp()), ZoneId.of("UTC"));
        InventoryMonitoringEntity record = new InventoryMonitoringEntity()
                .setInventory(inventory)
                .setLat(message.getLat())
                .setLng(message.getLng())
                .setSatellites(message.getSatellites())
                .setAltitude(message.getAltitude())
                .setSpeed(message.getSpeed())
                .setOriginalTimestamp(timestamp);

        monitoringRecord.add(record);
    }
}
