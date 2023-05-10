package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.FileEntity;
import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.entity.OrganizationTariffEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.integration.dadata.service.DadataOrganizationService;
import com.muravev.samokatimmonolit.model.OrganizationStatus;
import com.muravev.samokatimmonolit.model.in.command.organization.OrganizationChangeLogoCommand;
import com.muravev.samokatimmonolit.model.in.command.organization.OrganizationCreateCommand;
import com.muravev.samokatimmonolit.model.in.command.organization.TariffAddCommand;
import com.muravev.samokatimmonolit.model.in.command.organization.TariffDeleteCommand;
import com.muravev.samokatimmonolit.repo.FileRepo;
import com.muravev.samokatimmonolit.repo.OrganizationRepo;
import com.muravev.samokatimmonolit.repo.TariffRepo;
import com.muravev.samokatimmonolit.service.EmployeeSaver;
import com.muravev.samokatimmonolit.service.OrganizationSaver;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationSaverImpl implements OrganizationSaver {
    private final TariffRepo tariffRepo;
    private final OrganizationRepo organizationRepo;
    private final FileRepo fileRepo;

    private final EmployeeSaver employeeSaver;
    private final DadataOrganizationService dadataOrganizationService;
    private final SecurityService securityService;


    @Override
    @Transactional
    public OrganizationEntity create(OrganizationCreateCommand command) {
        organizationRepo.findByInn(command.inn())
                .ifPresent((any) -> {
                    throw new ApiException(StatusCode.ORGANIZATION_ALREADY_EXIST);
                });

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

    @Override
    @Transactional
    public OrganizationEntity addTariff(TariffAddCommand command) {
        EmployeeEntity employee = securityService.getCurrentEmployee();
        OrganizationEntity organization = employee.getOrganization();
        Set<OrganizationTariffEntity> existedTariffs = organization.getTariffs();
        OrganizationTariffEntity tariff = new OrganizationTariffEntity()
                .setType(command.type())
                .setPrice(command.price())
                .setName(command.alias())
                .setOrganization(organization);

        existedTariffs.add(tariff);
        return organization;
    }

    @Override
    @Transactional
    public OrganizationEntity deleteTariff(TariffDeleteCommand command) {
        EmployeeEntity employee = securityService.getCurrentEmployee();
        OrganizationTariffEntity tariff = tariffRepo.findByIdAndOrganization(command.tariffId(), employee.getOrganization())
                .orElseThrow(() -> new ApiException(StatusCode.TARIFF_NOT_FOUND));
        tariff.setDeletedAt(ZonedDateTime.now());
        return tariff.getOrganization();
    }

    @Override
    @Transactional
    public OrganizationEntity changeLogo(OrganizationChangeLogoCommand command) {
        EmployeeEntity employee = securityService.getCurrentEmployee();
        OrganizationEntity organization = employee.getOrganization();
        FileEntity file = fileRepo.getReferenceById(command.fileId());
        organization.setLogo(file);
        return organization;
    }
}
