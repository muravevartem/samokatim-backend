package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.InventoryEntity;
import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.mapper.InventoryMapper;
import com.muravev.samokatimmonolit.mapper.RentMapper;
import com.muravev.samokatimmonolit.model.in.MapViewIn;
import com.muravev.samokatimmonolit.model.out.MapViewOut;
import com.muravev.samokatimmonolit.service.InventoryReader;
import com.muravev.samokatimmonolit.service.MapService;
import com.muravev.samokatimmonolit.service.RentReader;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class MapServiceImpl implements MapService {
    private final InventoryMapper inventoryMapper;
    private final RentMapper rentMapper;

    private final InventoryReader inventoryReader;
    private final RentReader rentReader;


    @Override
    public MapViewOut getClientView(MapViewIn viewIn) {
        Collection<InventoryEntity> forRent = inventoryReader.findAllInViewForRent(viewIn);
        Collection<RentEntity> inView = rentReader.findAllInViewAndRentedByMe(viewIn);

        return new MapViewOut(
                forRent.stream()
                        .map(inventoryMapper::toFullDto)
                        .toList(),
                inView.stream()
                        .map(rentMapper::toDto)
                        .toList()
        );
    }
}
