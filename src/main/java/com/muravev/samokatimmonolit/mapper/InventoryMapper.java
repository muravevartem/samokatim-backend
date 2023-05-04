package com.muravev.samokatimmonolit.mapper;

import com.muravev.samokatimmonolit.entity.InventoryEntity;
import com.muravev.samokatimmonolit.model.out.InventoryFullOut;
import org.mapstruct.Mapper;

@Mapper(
        uses = {
                InventoryModelMapper.class,
                InventoryEventMappper.class
        }
)
public interface InventoryMapper {

    InventoryFullOut toFullDto(InventoryEntity entity);
}
