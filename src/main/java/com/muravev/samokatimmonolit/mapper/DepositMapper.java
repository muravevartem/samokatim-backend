package com.muravev.samokatimmonolit.mapper;

import com.muravev.samokatimmonolit.entity.DepositEntity;
import com.muravev.samokatimmonolit.model.out.DepositOut;
import org.mapstruct.Mapper;

@Mapper
public interface DepositMapper {
    DepositOut toDto(DepositEntity entity);
}
