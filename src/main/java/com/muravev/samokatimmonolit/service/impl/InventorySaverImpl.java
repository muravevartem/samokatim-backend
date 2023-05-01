package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.InventoryEntity;
import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.event.InventoryStatusChangedEvent;
import com.muravev.samokatimmonolit.model.InventoryStatus;
import com.muravev.samokatimmonolit.model.in.InventoryModelIn;
import com.muravev.samokatimmonolit.model.in.command.inventory.*;
import com.muravev.samokatimmonolit.repo.InventoryModelRepo;
import com.muravev.samokatimmonolit.repo.InventoryRepo;
import com.muravev.samokatimmonolit.service.InventorySaver;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventorySaverImpl implements InventorySaver {
    private final InventoryRepo inventoryRepo;
    private final InventoryModelRepo inventoryModelRepo;

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
        inventory.setStatus(InventoryStatus.UNDER_REPAIR);//По умолчанию на старте числиться в ремонте
        return inventoryRepo.save(inventory);
    }

    @Override
    @Transactional
    public InventoryEntity changeField(long id, InventoryChangeStatusCommand command) {
        InventoryEntity inventory = inventoryRepo.findById(id)
                .orElseThrow(() -> new ApiException(StatusCode.INVENTORY_NOT_FOUND));
        inventory.setStatus(command.status());
        eventPublisher.publishEvent(InventoryStatusChangedEvent.of(inventory, command.status()));
        return inventory;
    }

    @Override
    @Transactional
    public InventoryEntity changeField(long id, InventoryChangeAliasCommand command) {
        InventoryEntity inventory = inventoryRepo.findById(id)
                .orElseThrow(() -> new ApiException(StatusCode.INVENTORY_NOT_FOUND));
        inventory.setAlias(command.alias());
        return inventory;
    }

    @Override
    @Transactional
    public InventoryEntity changeField(long id, InventoryChangeModelCommand command) {
        InventoryEntity inventory = inventoryRepo.findById(id)
                .orElseThrow(() -> new ApiException(StatusCode.INVENTORY_NOT_FOUND));
        InventoryModelIn model = command.model();
        inventory.setModel(inventoryModelRepo.getReferenceById(model.id()));
        return inventory;
    }

    @Override
    @Transactional
    public InventoryEntity changeField(long id, InventoryChangeClassCommand command) {
        InventoryEntity inventory = inventoryRepo.findById(id)
                .orElseThrow(() -> new ApiException(StatusCode.INVENTORY_NOT_FOUND));
        inventory.setInventoryClass(command.inventoryClass());
        return inventory;
    }

    @Override
    @Transactional
    public void delete(long id) {
        inventoryRepo.deleteById(id);
    }
}
