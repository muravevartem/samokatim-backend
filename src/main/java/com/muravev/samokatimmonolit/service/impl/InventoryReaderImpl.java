package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.*;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.event.AbstractInventoryEvent;
import com.muravev.samokatimmonolit.model.InventoryStatus;
import com.muravev.samokatimmonolit.model.in.MapViewIn;
import com.muravev.samokatimmonolit.repo.InventoryRepo;
import com.muravev.samokatimmonolit.service.InventoryReader;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.SortedSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryReaderImpl implements InventoryReader {
    private final InventoryRepo inventoryRepo;

    private final SecurityService securityService;

    @Override
    @Transactional(readOnly = true)
    public Collection<InventoryEntity> findAllInViewForRent(MapViewIn view) {
        var northEast = view.getNorthEast();
        var southWest = view.getSouthWest();
        log.info("[GEO] getting location ne-lat: {} ne-lng: {} sw-lat: {} sw-lng: {}",
                northEast.lat(), northEast.lng(), southWest.lat(), southWest.lng());
        return inventoryRepo.findAllByViewAndStatus(
                northEast.lat(),
                northEast.lng(),
                southWest.lat(),
                southWest.lng(),
                InventoryStatus.PENDING
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<InventoryEntity> findAllInViewAndRentedByMe(MapViewIn view) {
        ClientEntity currentClient = securityService.getCurrentClient();

        var northEast = view.getNorthEast();
        var southWest = view.getSouthWest();
        log.info("[GEO] getting location ne-lat: {} ne-lng: {} sw-lat: {} sw-lng: {}",
                northEast.lat(), northEast.lng(), southWest.lat(), southWest.lng());
        return inventoryRepo.findAllRentedByView(
                northEast.lat(),
                northEast.lng(),
                southWest.lat(),
                southWest.lng(),
                currentClient
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventoryEntity> findAllAsEmployee(String keyword, Pageable pageable) {
        EmployeeEntity currentEmployee = securityService.getCurrentEmployee();
        OrganizationEntity organization = currentEmployee.getOrganization();

        if (keyword == null || keyword.isBlank()) {
            return inventoryRepo.findAllByOrganization(organization, pageable);
        }
        return inventoryRepo.findAllByOrganization(keyword, organization, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryEntity findByIdAsEmployee(long id) {
        EmployeeEntity currentEmployee = securityService.getCurrentEmployee();
        return inventoryRepo.findById(id)
                .filter(inventory -> Objects.equals(inventory.getOrganization(), currentEmployee.getOrganization()))
                .orElseThrow(() -> new ApiException(StatusCode.INVENTORY_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryEntity findByIdAsClient(long id) {
        InventoryEntity inventory = inventoryRepo.findById(id)
                .orElseThrow(() -> new ApiException(StatusCode.INVENTORY_NOT_FOUND));

        if (inventory.getStatus() == InventoryStatus.PENDING)
            return inventory;

        ClientEntity currentClient = securityService.getCurrentClient();
        RentEntity activeRent = inventory.getActiveRent();
        if (activeRent != null && Objects.equals(activeRent.getClient(), currentClient)) {
            return inventory;
        }
        throw new ApiException(StatusCode.INVENTORY_NOT_FOUND);
    }

    @Override
    @Transactional(readOnly = true)
    public SortedSet<AbstractInventoryEvent> findEventsById(long id) {
        InventoryEntity inventory = inventoryRepo.findById(id)
                .orElseThrow(() -> new ApiException(StatusCode.INVENTORY_NOT_FOUND));

        return inventory.getEvents();
    }

}
