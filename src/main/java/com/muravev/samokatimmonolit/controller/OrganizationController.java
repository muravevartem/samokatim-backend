package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.mapper.OrganizationMapper;
import com.muravev.samokatimmonolit.model.in.command.organization.OrganizationChangeLogoCommand;
import com.muravev.samokatimmonolit.model.in.command.organization.OrganizationCreateCommand;
import com.muravev.samokatimmonolit.model.in.command.organization.TariffAddCommand;
import com.muravev.samokatimmonolit.model.in.command.organization.TariffDeleteCommand;
import com.muravev.samokatimmonolit.model.out.OrganizationCompactOut;
import com.muravev.samokatimmonolit.model.out.OrganizationFullOut;
import com.muravev.samokatimmonolit.model.out.TariffOut;
import com.muravev.samokatimmonolit.service.OrganizationReader;
import com.muravev.samokatimmonolit.service.OrganizationSaver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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

    @GetMapping("/me")
    public OrganizationFullOut getMyOrg() {
        OrganizationEntity myOrg = organizationReader.getMyOrg();
        return organizationMapper.toFullDto(myOrg);
    }



    @PostMapping
    public OrganizationFullOut register(@RequestBody @Valid OrganizationCreateCommand command) {
        OrganizationEntity createdOrganization = organizationSaver.create(command);
        return organizationMapper.toFullDto(createdOrganization);
    }

    @PostMapping("/me/tariffs")
    public OrganizationFullOut addTariff(@Valid @RequestBody TariffAddCommand command) {
        OrganizationEntity organization = organizationSaver.addTariff(command);
        return organizationMapper.toFullDto(organization);
    }

    @PostMapping("/me/logo")
    public OrganizationFullOut changeAvatar(@Valid @RequestBody OrganizationChangeLogoCommand command) {
        OrganizationEntity organization = organizationSaver.changeLogo(command);
        return organizationMapper.toFullDto(organization);
    }


    @DeleteMapping("/me/tariffs/{id}")
    public OrganizationFullOut OrganizationFullOut(@PathVariable long id) {
        OrganizationEntity organization = organizationSaver.deleteTariff(new TariffDeleteCommand(id));
        return organizationMapper.toFullDto(organization);
    }

    @GetMapping("/me/tariffs")
    public Set<TariffOut> getTariffs() {
        return getMyOrg().tariffs();
    }

}
