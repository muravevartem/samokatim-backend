package com.muravev.samokatimmonolit.mapper;

import com.muravev.samokatimmonolit.entity.ChequeEntity;
import com.muravev.samokatimmonolit.model.out.ChequeOut;
import org.mapstruct.Mapper;

@Mapper
public interface ChequeMapper {
    ChequeOut toDto(ChequeEntity entity);
}
