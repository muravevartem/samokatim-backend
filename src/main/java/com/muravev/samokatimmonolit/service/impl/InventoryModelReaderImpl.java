package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.InventoryModelEntity;
import com.muravev.samokatimmonolit.entity.UserEntity;
import com.muravev.samokatimmonolit.repo.InventoryModelRepo;
import com.muravev.samokatimmonolit.service.InventoryModelReader;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryModelReaderImpl implements InventoryModelReader {
    private final InventoryModelRepo inventoryModelRepo;
    private final SecurityService securityService;


    @Override
    @Transactional(readOnly = true)
    public List<InventoryModelEntity> findAll(String keyword) {
        UserEntity currentClient = securityService.getCurrentUser();
        if (keyword.isEmpty())
            return inventoryModelRepo.findAllByApproved(currentClient);
        return inventoryModelRepo.findAllByApproved(currentClient, keyword);
    }
}
