package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.InventoryManufactureEntity;
import com.muravev.samokatimmonolit.entity.InventoryModelEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.model.in.command.inventorymodel.InventoryModelChangeApprovingCommand;
import com.muravev.samokatimmonolit.model.in.command.inventorymodel.InventoryModelCreateCommand;
import com.muravev.samokatimmonolit.repo.InventoryManufactureRepo;
import com.muravev.samokatimmonolit.repo.InventoryModelRepo;
import com.muravev.samokatimmonolit.service.InventoryModelSaver;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.muravev.samokatimmonolit.util.StringUtil.normal;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryModelSaverImpl implements InventoryModelSaver {
    private final InventoryModelRepo inventoryModelRepo;
    private final InventoryManufactureRepo manufactureRepo;
    private final SecurityService securityService;


    @Override
    @Transactional
    public InventoryModelEntity create(InventoryModelCreateCommand createCommand) {
        InventoryModelEntity modelEntity = new InventoryModelEntity();
        modelEntity.setName(createCommand.name());
        modelEntity.setType(createCommand.type());

        String manufactureName = createCommand.manufacture();
        InventoryManufactureEntity manufacture = manufactureRepo.findByNameIgnoreCase(manufactureName)
                .orElseGet(() -> create(manufactureName));

        modelEntity.setManufacture(manufacture);
        modelEntity.setApproved(false);
        return inventoryModelRepo.save(modelEntity);
    }

    private InventoryManufactureEntity create(String name) {
        InventoryManufactureEntity manufacture = new InventoryManufactureEntity()
                .setName(normal(name));
        return manufactureRepo.save(manufacture);
    }

    @Override
    @Transactional
    public InventoryModelEntity updateApprove(long id, InventoryModelChangeApprovingCommand command) {
        InventoryModelEntity inventoryModel = inventoryModelRepo.findById(id)
                .orElseThrow(() -> new ApiException(StatusCode.INVENTORY_MODEL_NOT_FOUND));
        inventoryModel.setApproved(command.approved());
        return inventoryModel;
    }

    @Override
    @Transactional
    public void delete(long id) {
        InventoryModelEntity modelEntity = inventoryModelRepo.findById(id)
                .orElseThrow(() -> new ApiException(StatusCode.INVENTORY_MODEL_NOT_FOUND));
        if (modelEntity.isApproved())
            throw new ApiException(StatusCode.INVENTORY_MODEL_CANNOT_REMOVED);

        boolean isAuthor = Objects.equals(modelEntity.getCreatedBy(), securityService.getCurrentEmployee());
        boolean hasRegisteredInventory = modelEntity.getInventories().isEmpty();
        if (!isAuthor || hasRegisteredInventory) {
            throw new ApiException(StatusCode.INVENTORY_MODEL_CANNOT_REMOVED);
        }
        inventoryModelRepo.delete(modelEntity);
    }


}
