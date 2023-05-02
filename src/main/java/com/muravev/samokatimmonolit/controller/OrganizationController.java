package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.mapper.OrganizationMapper;
import com.muravev.samokatimmonolit.model.in.command.organization.OrganizationCreateCommand;
import com.muravev.samokatimmonolit.model.out.OrganizationCompactOut;
import com.muravev.samokatimmonolit.model.out.OrganizationFullOut;
import com.muravev.samokatimmonolit.service.OrganizationReader;
import com.muravev.samokatimmonolit.service.OrganizationSaver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orgs")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationMapper organizationMapper;
    private final OrganizationSaver organizationSaver;
    private final OrganizationReader organizationReader;


    @GetMapping(params = "inn")
    public OrganizationCompactOut getByInn(@RequestParam("inn") String inn) {
        OrganizationEntity organization = organizationReader.getByInn(inn);
        return organizationMapper.toCompactDto(organization);
    }

    @PostMapping
    public OrganizationFullOut register(@RequestBody @Valid OrganizationCreateCommand command) {
        OrganizationEntity createdOrganization = organizationSaver.create(command);
        return organizationMapper.toFullDto(createdOrganization);
    }
}
