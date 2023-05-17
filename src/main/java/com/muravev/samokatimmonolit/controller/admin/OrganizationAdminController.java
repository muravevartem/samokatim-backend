package com.muravev.samokatimmonolit.controller.admin;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.mapper.OrganizationMapper;
import com.muravev.samokatimmonolit.model.out.OrganizationFullOut;
import com.muravev.samokatimmonolit.service.OrganizationReader;
import com.muravev.samokatimmonolit.service.OrganizationSaver;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/orgs")
@RequiredArgsConstructor
public class OrganizationAdminController {
    private final OrganizationMapper organizationMapper;
    private final OrganizationSaver organizationSaver;
    private final OrganizationReader organizationReader;


    @GetMapping(params = {"size", "page", "keyword"})
    public Page<OrganizationFullOut> getOrganizations(@RequestParam String keyword, Pageable pageable) {
        return organizationReader.getAll(keyword, pageable)
                .map(organizationMapper::toFullDto);
    }

    @GetMapping("/{id}")
    public OrganizationFullOut getOrganizations(@PathVariable long id) {
        OrganizationEntity organization = organizationReader.getOne(id);
        return organizationMapper.toFullDto(organization);
    }

    @GetMapping(params = {"inn"})
    public OrganizationFullOut getOrganizations(@RequestParam String inn) {
        OrganizationEntity organization = organizationReader.getByInn(inn);
        return organizationMapper.toFullDto(organization);
    }

    @PutMapping("/{id}/verify")
    public OrganizationFullOut verify(@PathVariable long id) {
        OrganizationEntity organization = organizationSaver.verify(id);
        return organizationMapper.toFullDto(organization);
    }

    @PutMapping("/{id}/block")
    public OrganizationFullOut block(@PathVariable long id) {
        OrganizationEntity organization = organizationSaver.block(id);
        return organizationMapper.toFullDto(organization);
    }

    @PutMapping("/{id}/unblock")
    public OrganizationFullOut unblock(@PathVariable long id) {
        OrganizationEntity organization = organizationSaver.unblock(id);
        return organizationMapper.toFullDto(organization);
    }
}
