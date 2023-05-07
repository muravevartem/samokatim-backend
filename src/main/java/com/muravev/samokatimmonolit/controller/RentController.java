package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.mapper.RentMapper;
import com.muravev.samokatimmonolit.model.in.command.rent.RentCreateCommand;
import com.muravev.samokatimmonolit.model.out.RentOut;
import com.muravev.samokatimmonolit.service.RentReader;
import com.muravev.samokatimmonolit.service.RentSaver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rents")
@RequiredArgsConstructor
public class RentController {
    private final RentSaver rentSaver;
    private final RentReader rentReader;
    private final RentMapper rentMapper;


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
