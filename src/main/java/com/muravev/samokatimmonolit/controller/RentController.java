package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.entity.InventoryMonitoringEntity;
import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.Payment;
import com.muravev.samokatimmonolit.integration.yookassa.service.YooKassaPaymentService;
import com.muravev.samokatimmonolit.mapper.InventoryMonitoringRecordMapper;
import com.muravev.samokatimmonolit.mapper.RentMapper;
import com.muravev.samokatimmonolit.model.in.command.rent.RentCreateCommand;
import com.muravev.samokatimmonolit.model.in.command.rent.RentStopCommand;
import com.muravev.samokatimmonolit.model.out.GeoPositionOut;
import com.muravev.samokatimmonolit.model.out.PaymentOptionsOut;
import com.muravev.samokatimmonolit.model.out.RentCompactOut;
import com.muravev.samokatimmonolit.model.out.RentOut;
import com.muravev.samokatimmonolit.service.PaymentService;
import com.muravev.samokatimmonolit.service.RentReader;
import com.muravev.samokatimmonolit.service.RentSaver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping(params = {"my-org", "page", "size"})
    public Page<RentOut> findAll(Pageable pageable) {
        return rentReader.findMyOrgAll(pageable)
                .map(rentMapper::toDto);
    }

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

    @GetMapping(value = "/{id}", params = "full")
    public RentOut findById(@PathVariable long id) {
        RentEntity rent = rentReader.findById(id);
        return rentMapper.toDto(rent);
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
    public PaymentOptionsOut create(@RequestBody @Valid RentCreateCommand command) {
        return rentSaver.start(command);
    }

    @PutMapping("/{id}/stop")
    public void stop(@PathVariable long id, @RequestBody RentStopCommand command) {
        rentSaver.stop(id, command);
    }

    @PutMapping("/{id}/complete")
    public PaymentOptionsOut end(@PathVariable long id) {
        return rentSaver.end(id);
    }

    @PutMapping("/{id}/repay")
    public PaymentOptionsOut repay(@PathVariable long id) {
        return rentSaver.repay(id);
    }

}
