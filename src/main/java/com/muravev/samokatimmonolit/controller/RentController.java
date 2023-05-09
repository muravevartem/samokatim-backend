package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.entity.InventoryMonitoringEntity;
import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.mapper.InventoryMonitoringRecordMapper;
import com.muravev.samokatimmonolit.mapper.RentMapper;
import com.muravev.samokatimmonolit.model.in.command.rent.RentCreateCommand;
import com.muravev.samokatimmonolit.model.out.GeoPositionOut;
import com.muravev.samokatimmonolit.model.out.RentCompactOut;
import com.muravev.samokatimmonolit.model.out.RentOut;
import com.muravev.samokatimmonolit.service.RentReader;
import com.muravev.samokatimmonolit.service.RentSaver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.SortedSet;

@RestController
@RequestMapping("/api/v1/rents")
@RequiredArgsConstructor
public class RentController {
    private final RentSaver rentSaver;
    private final RentReader rentReader;
    private final RentMapper rentMapper;
    private final InventoryMonitoringRecordMapper monitoringRecordMapper;

    @GetMapping(params = {"my", "active"})
    public List<RentOut> findMyActiveAll() {
        List<RentEntity> activeRents = rentReader.findMyActiveAll();
        return activeRents.stream()
                .map(rentMapper::toDto)
                .toList();
    }

    @GetMapping(params = {"my", "page", "size"})
    public Page<RentCompactOut> findMyAll(Pageable pageable) {
        return rentReader.findMyAll(pageable)
                .map(rentMapper::toCompactDto);
    }

    @GetMapping("/{id}")
    public RentOut findMyById(@PathVariable long id) {
        RentEntity rent = rentReader.findMyById(id);
        return rentMapper.toDto(rent);
    }

    @GetMapping("/{id}/track")
    public List<GeoPositionOut> findTrackByRentId(@PathVariable long id) {
        SortedSet<InventoryMonitoringEntity> track = rentReader.getTrack(id);
        return track.stream()
                .map(monitoringRecordMapper::toGeoposiotion)
                .toList();
    }


    @PostMapping
    public RentOut create(@RequestBody @Valid RentCreateCommand command) {
        RentEntity rent = rentSaver.start(command);
        return rentMapper.toDto(rent);
    }

    @PutMapping("/{id}/complete")
    public RentOut end(@PathVariable long id) {
        RentEntity rent = rentSaver.end(id);
        return rentMapper.toDto(rent);
    }
}
