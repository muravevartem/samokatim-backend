package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.integration.dadata.service.DadataOrganizationService;
import com.muravev.samokatimmonolit.model.OrganizationStatus;
import com.muravev.samokatimmonolit.model.in.command.organization.OrganizationCreateCommand;
import com.muravev.samokatimmonolit.repo.OrganizationRepo;
import com.muravev.samokatimmonolit.service.EmployeeSaver;
import com.muravev.samokatimmonolit.service.OrganizationSaver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.metrics.Stat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationSaverImpl implements OrganizationSaver {
    private final OrganizationRepo organizationRepo;
    private final EmployeeSaver employeeSaver;
    private final DadataOrganizationService dadataOrganizationService;


    @Override
    @Transactional
    public OrganizationEntity create(OrganizationCreateCommand command) {
        organizationRepo.findByInn(command.inn())
                .ifPresent((any)->{ throw new ApiException(StatusCode.ORGANIZATION_ALREADY_EXIST);});

        OrganizationEntity organization = dadataOrganizationService.getOneByInn(command.inn())
                .orElseThrow(() -> new ApiException(StatusCode.ORGANIZATION_NOT_FOUND))
                .setEmail(command.email().toLowerCase())
                .setTel(command.tel())
                .setStatus(OrganizationStatus.PENDING);

        inviteFatherEmployee(organization);

        return organizationRepo.save(organization);
    }

    private void inviteFatherEmployee(OrganizationEntity organization) {
        employeeSaver.invite(organization.getEmail(), organization);
    }
}
