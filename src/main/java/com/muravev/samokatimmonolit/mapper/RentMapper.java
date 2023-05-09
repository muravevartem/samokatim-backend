package com.muravev.samokatimmonolit.mapper;

import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.model.out.RentCompactOut;
import com.muravev.samokatimmonolit.model.out.RentOut;
import org.mapstruct.Mapper;

@Mapper(uses = {
        InventoryMapper.class,
        TariffMapper.class,
        ChequeMapper.class,
        InventoryMonitoringRecordMapper.class
})
public interface RentMapper {
    RentOut toDto(RentEntity entity);

    RentCompactOut toCompactDto(RentEntity entity);
}
