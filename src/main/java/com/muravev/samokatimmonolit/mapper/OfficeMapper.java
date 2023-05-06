package com.muravev.samokatimmonolit.mapper;

import com.muravev.samokatimmonolit.entity.OfficeEntity;
import com.muravev.samokatimmonolit.entity.OfficeScheduleEmbeddable;
import com.muravev.samokatimmonolit.model.out.OfficeFullOut;
import com.muravev.samokatimmonolit.model.out.OfficeScheduleOut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
        InventoryMapper.class,
        OrganizationMapper.class
})
public interface OfficeMapper {
    OfficeFullOut toFullDto(OfficeEntity entity);

    @Mapping(target = "dayOff", source = "dayOff")
    OfficeScheduleOut toDto(OfficeScheduleEmbeddable embeddable);
}
