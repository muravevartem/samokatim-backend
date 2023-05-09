package com.muravev.samokatimmonolit.mapper;

import com.muravev.samokatimmonolit.entity.InventoryMonitoringEntity;
import com.muravev.samokatimmonolit.model.out.GeoPositionOut;
import org.mapstruct.Mapper;

@Mapper
public interface InventoryMonitoringRecordMapper {
    GeoPositionOut toGeoposiotion(InventoryMonitoringEntity entity);
}
