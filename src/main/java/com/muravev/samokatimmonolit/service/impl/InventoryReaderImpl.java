package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.*;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.model.InventoryStatus;
import com.muravev.samokatimmonolit.repo.InventoryRepo;
import com.muravev.samokatimmonolit.service.InventoryReader;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryReaderImpl implements InventoryReader {
    private final InventoryRepo inventoryRepo;

    private final SecurityService securityService;

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
        if (activeRent != null && Objects.equals(activeRent.getClientEntity(), currentClient)) {
            return inventory;
        }
        throw new ApiException(StatusCode.INVENTORY_NOT_FOUND);
    }


}
