package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.mapper.RentMapper;
import com.muravev.samokatimmonolit.model.in.command.rent.RentCreateCommand;
import com.muravev.samokatimmonolit.model.out.RentOut;
import com.muravev.samokatimmonolit.service.RentReader;
import com.muravev.samokatimmonolit.service.RentSaver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rents")
@RequiredArgsConstructor
public class RentController {
    private final RentSaver rentSaver;
    private final RentReader rentReader;
    private final RentMapper rentMapper;


    @GetMapping(params = {"my", "page", "size"})
    public Page<RentOut> findMyAll(Pageable pageable) {
        return rentReader.findMyAll(pageable)
                .map(rentMapper::toDto);
    }

    @GetMapping("/{id}")
    public RentOut findMyById(@PathVariable long id) {
        RentEntity rent = rentReader.findMyById(id);
        return rentMapper.toDto(rent);
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
