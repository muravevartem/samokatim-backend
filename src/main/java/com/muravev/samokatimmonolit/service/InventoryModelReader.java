package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.InventoryModelEntity;

import java.util.List;

public interface InventoryModelReader {

    List<InventoryModelEntity> findAll(String keyword);
}
