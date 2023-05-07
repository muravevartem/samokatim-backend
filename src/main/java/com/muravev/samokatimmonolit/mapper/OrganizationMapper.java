package com.muravev.samokatimmonolit.mapper;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.model.out.OrganizationCompactOut;
import com.muravev.samokatimmonolit.model.out.OrganizationFullOut;
import org.mapstruct.Mapper;

@Mapper(
        uses = TariffMapper.class
)
public interface OrganizationMapper {
    OrganizationFullOut toFullDto(OrganizationEntity entity);

    OrganizationCompactOut toCompactDto(OrganizationEntity entity);
}
