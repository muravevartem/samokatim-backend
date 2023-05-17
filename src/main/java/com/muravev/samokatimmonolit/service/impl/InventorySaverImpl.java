package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmessage.GeoPointNewMessage;
import com.muravev.samokatimmonolit.entity.*;
import com.muravev.samokatimmonolit.entity.user.EmployeeEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.event.InventoryStatusChangedEvent;
import com.muravev.samokatimmonolit.model.InventoryStatus;
import com.muravev.samokatimmonolit.model.InventoryType;
import com.muravev.samokatimmonolit.model.in.InventoryModelIn;
import com.muravev.samokatimmonolit.model.in.command.inventory.*;
import com.muravev.samokatimmonolit.repo.InventoryModelRepo;
import com.muravev.samokatimmonolit.repo.InventoryRepo;
import com.muravev.samokatimmonolit.repo.OfficeRepo;
import com.muravev.samokatimmonolit.repo.TariffRepo;
import com.muravev.samokatimmonolit.service.InventorySaver;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
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
    private final OfficeRepo officeRepo;

    private final SecurityService securityService;

    private final ApplicationEventPublisher eventPublisher;


    @Override
    @Transactional
    public InventoryEntity create(InventoryCreateCommand createCommand) {
        EmployeeEntity currentEmployee = securityService.getCurrentEmployee();
        OrganizationEntity organization = currentEmployee.getOrganization();

        InventoryModelIn model = createCommand.model();

        InventoryModelEntity inventoryModel = inventoryModelRepo.findById(model.id())
                .orElseThrow(() -> new ApiException(StatusCode.INVENTORY_MODEL_NOT_FOUND));

        InventoryType inventoryType = inventoryModel.getType();

        InventoryEntity inventory = new InventoryEntity();
        inventory.setInventoryClass(createCommand.inventoryClass());
        inventory.setModel(inventoryModel);
        inventory.setOrganization(organization);
        inventory.setSupportsTelemetry(createCommand.supportsTelemetry());
        inventory.setStatus(InventoryStatus.UNDER_REPAIR);//По умолчанию на старте числиться в ремонте
        InventoryEntity save = inventoryRepo.save(inventory);
        String formattedId = StringUtils.leftPad(save.getId().toString(), 6, "0");
        String alias = formattedId.substring(0, 3) + "-" + formattedId.substring(3, 6) + "-" + organization.getId();
        save.setAlias(alias);
        return save;
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

    @Override
    @Transactional
    public InventoryEntity changeField(long id, InventoryChangeOfficeCommand command) {
        InventoryEntity inventory = getOneAsEmployee(id);
        inventory.setOffice(officeRepo.getReferenceById(command.officeId()));
        return inventory;
    }

    @Override
    @Transactional
    public InventoryEntity changeField(long id, InventoryResetOfficeCommand command) {
        InventoryEntity inventory = getOneAsEmployee(id);
        inventory.setOffice(null);
        return inventory;
    }

    @Override
    @Transactional
    public InventoryEntity changeStatus(InventoryEntity inventory, InventoryStatus status) {
        inventory.setStatus(status);
        eventPublisher.publishEvent(InventoryStatusChangedEvent.of(inventory, InventoryStatus.IN_WORK));
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
    public void savePoint(GeoPointNewMessage message) {
        String inventoryId = message.getClientId();
        log.info("New geo point for inventory {}", inventoryId);
        InventoryEntity inventory = inventoryRepo.findByAlias(inventoryId)
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
