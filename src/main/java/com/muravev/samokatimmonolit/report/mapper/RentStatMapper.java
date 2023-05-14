package com.muravev.samokatimmonolit.report.mapper;

import com.muravev.samokatimmonolit.report.enity.RentStatEntityView;
import com.muravev.samokatimmonolit.report.model.out.RentStatOut;
import org.mapstruct.Mapper;

@Mapper
public interface RentStatMapper {
    RentStatOut toDto(RentStatEntityView entityView);
}
