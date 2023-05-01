package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.InventoryModelEntity;
import com.muravev.samokatimmonolit.model.in.command.inventorymodel.InventoryModelCreateCommand;
import com.muravev.samokatimmonolit.model.in.command.inventorymodel.InventoryModelChangeApprovingCommand;
import org.springframework.transaction.annotation.Transactional;

public interface InventoryModelSaver {
    InventoryModelEntity create(InventoryModelCreateCommand createCommand);

    InventoryModelEntity updateApprove(long id, InventoryModelChangeApprovingCommand command);

    @Transactional
    void delete(long id);
}
