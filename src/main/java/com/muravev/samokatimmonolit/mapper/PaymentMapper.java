package com.muravev.samokatimmonolit.mapper;

import com.muravev.samokatimmonolit.entity.PaymentEntity;
import com.muravev.samokatimmonolit.model.out.PaymentOut;
import org.mapstruct.Mapper;

@Mapper
public interface PaymentMapper {
    PaymentOut toDto(PaymentEntity entity);
}
