package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.entity.OfficeEntity;
import com.muravev.samokatimmonolit.mapper.OfficeMapper;
import com.muravev.samokatimmonolit.model.in.command.office.OfficeCreateCommand;
import com.muravev.samokatimmonolit.model.out.OfficeFullOut;
import com.muravev.samokatimmonolit.service.OfficeReader;
import com.muravev.samokatimmonolit.service.OfficeWriter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/offices")
@RequiredArgsConstructor
public class OfficeController {
    private final OfficeWriter officeWriter;
    private final OfficeMapper officeMapper;
    private final OfficeReader officeReader;


    @GetMapping(params = {"my", "keyword"})
    public Page<OfficeFullOut> getAllMy(@RequestParam String keyword, Pageable pageable) {
        Page<OfficeEntity> allMyOffices = officeReader.getAllMyOffices(keyword, pageable);
        return allMyOffices.map(officeMapper::toFullDto);
    }

    @GetMapping(params = "my")
    public Page<OfficeFullOut> getAllMy(Pageable pageable) {
        Page<OfficeEntity> allMyOffices = officeReader.getAllMyOffices(pageable);
        return allMyOffices.map(officeMapper::toFullDto);
    }

    @GetMapping(value = "/{id}", params = "my")
    public OfficeFullOut getOne(@PathVariable long id) {
        OfficeEntity oneMy = officeReader.getOneMy(id);
        return officeMapper.toFullDto(oneMy);
    }

    @PostMapping
    public OfficeFullOut create(@RequestBody @Valid OfficeCreateCommand command) {
        OfficeEntity office = officeWriter.create(command);
        return officeMapper.toFullDto(office);
    }

}
