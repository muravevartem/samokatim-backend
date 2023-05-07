package com.muravev.samokatimmonolit.mapper;

import com.muravev.samokatimmonolit.entity.OrganizationTariffEntity;
import com.muravev.samokatimmonolit.model.out.TariffOut;
import org.mapstruct.Mapper;

@Mapper
public interface TariffMapper {
    TariffOut toDto(OrganizationTariffEntity entity);
}
