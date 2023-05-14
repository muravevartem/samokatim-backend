package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.*;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.integration.dadata.service.DadataOrganizationService;
import com.muravev.samokatimmonolit.model.OrganizationStatus;
import com.muravev.samokatimmonolit.model.OrganizationTariffType;
import com.muravev.samokatimmonolit.model.in.command.organization.*;
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
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
        OrganizationTariffEntity tariff = switch (command.type()) {
            case MINUTE_BY_MINUTE -> new OrganizationTariffEntity()
                    .setType(OrganizationTariffType.MINUTE_BY_MINUTE)
                    .setPrice(command.price())
                    .setInitialPrice(command.initialPrice())
                    .setDeposit(command.deposit())
                    .setName(command.alias())
                    .setDays(new TreeSet<>(command.days()))
                    .setOrganization(organization);
            case LONG_TERM -> new OrganizationTariffEntity()
                    .setType(OrganizationTariffType.LONG_TERM)
                    .setPrice(command.price())
                    .setDeposit(command.deposit())
                    .setName(command.alias())
                    .setDays(new TreeSet<>(command.days()))
                    .setOrganization(organization);
        };

        existedTariffs.add(tariff);
        return organization;
    }

    @Override
    @Transactional
    public OrganizationEntity changeTariff(long id, TariffChangeCommand command) {
        OrganizationTariffEntity tariff = tariffRepo.findById(id)
                .orElseThrow(() -> new ApiException(StatusCode.TARIFF_NOT_FOUND));
        Set<InventoryEntity> inventories = tariff.getInventories();
        OrganizationTariffEntity updated = switch (command.type()) {
            case MINUTE_BY_MINUTE -> new OrganizationTariffEntity()
                    .setType(OrganizationTariffType.MINUTE_BY_MINUTE)
                    .setPrice(command.price())
                    .setInitialPrice(command.initialPrice())
                    .setDeposit(command.deposit())
                    .setName(command.alias())
                    .setInventories(new HashSet<>(inventories))
                    .setOrganization(tariff.getOrganization());
            case LONG_TERM -> new OrganizationTariffEntity()
                    .setType(OrganizationTariffType.LONG_TERM)
                    .setPrice(command.price())
                    .setDeposit(command.deposit())
                    .setName(command.alias())
                    .setInventories(new HashSet<>(inventories))
                    .setOrganization(tariff.getOrganization());
        };
        OrganizationTariffEntity savedTariff = tariffRepo.save(updated);
        OrganizationTariffEntity deletedTariff = tariff.setDeletedAt(ZonedDateTime.now());
        tariffRepo.save(deletedTariff);
        return savedTariff.getOrganization();
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
