package com.muravev.samokatimmonolit.mapper;

import com.muravev.samokatimmonolit.event.AbstractInventoryEvent;
import com.muravev.samokatimmonolit.model.out.InventoryEventOut;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class InventoryEventMappper {

    @Autowired
    private UserMapper userMapper;


    public InventoryEventOut toDto(AbstractInventoryEvent event) {
        if (event == null)
            return null;

        return new InventoryEventOut(
                event.getId(),
                event.getType(),
                event.getBody(),
                event.getCreatedAt(),
                userMapper.toDto(event.getCreatedBy())
        );
    }
}
