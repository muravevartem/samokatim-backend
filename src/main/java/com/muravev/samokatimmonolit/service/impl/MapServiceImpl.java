package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.InventoryEntity;
import com.muravev.samokatimmonolit.entity.OfficeEntity;
import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.mapper.InventoryMapper;
import com.muravev.samokatimmonolit.mapper.OfficeMapper;
import com.muravev.samokatimmonolit.mapper.RentMapper;
import com.muravev.samokatimmonolit.model.in.MapViewIn;
import com.muravev.samokatimmonolit.model.out.MapViewOut;
import com.muravev.samokatimmonolit.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MapServiceImpl implements MapService {
    private final InventoryMapper inventoryMapper;
    private final RentMapper rentMapper;
    private final OfficeMapper officeMapper;

    private final InventoryReader inventoryReader;
    private final RentReader rentReader;
    private final OfficeReader officeReader;


    @Override
    public MapViewOut getClientView(MapViewIn viewIn) {
        Collection<InventoryEntity> forRent = inventoryReader.findAllInViewForRent(viewIn);
        Collection<RentEntity> rented = rentReader.findAllInViewAndRentedByMe(viewIn);
        Collection<OfficeEntity> offices = officeReader.findAllInView(viewIn);

        return new MapViewOut(
                forRent.stream()
                        .map(inventoryMapper::toFullDto)
                        .toList(),
                rented.stream()
                        .map(rentMapper::toDto)
                        .toList(),
                offices.stream()
                        .map(officeMapper::toFullDto)
                        .toList()
        );
    }

    @Override
    public MapViewOut getMyOfficesView(MapViewIn viewIn) {
        Collection<OfficeEntity> offices = officeReader.findMyAllInView(viewIn);

        return new MapViewOut(
                List.of(),
                List.of(),
                offices.stream()
                        .map(officeMapper::toFullDto)
                        .toList()
        );
    }
}
