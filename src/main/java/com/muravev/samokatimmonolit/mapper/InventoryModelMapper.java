package com.muravev.samokatimmonolit.mapper;

import com.muravev.samokatimmonolit.entity.InventoryModelEntity;
import com.muravev.samokatimmonolit.model.out.InventoryModelFullOut;
import org.mapstruct.Mapper;

@Mapper
public interface InventoryModelMapper {
    InventoryModelFullOut toFullDto(InventoryModelEntity entity);
}
