package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.mapper.OrganizationMapper;
import com.muravev.samokatimmonolit.model.in.command.organization.OrganizationCreateCommand;
import com.muravev.samokatimmonolit.model.out.OrganizationFullOut;
import com.muravev.samokatimmonolit.service.OrganizationSaver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orgs")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationMapper organizationMapper;
    private final OrganizationSaver organizationSaver;


    @PostMapping
    public OrganizationFullOut register(@RequestBody @Valid OrganizationCreateCommand command) {
        OrganizationEntity createdOrganization = organizationSaver.create(command);
        return organizationMapper.toFullDto(createdOrganization);
    }
}
