package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.model.OrganizationStatus;
import com.muravev.samokatimmonolit.model.in.command.organization.OrganizationCreateCommand;
import com.muravev.samokatimmonolit.repo.OrganizationRepo;
import com.muravev.samokatimmonolit.service.EmployeeSaver;
import com.muravev.samokatimmonolit.service.OrganizationSaver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationSaverImpl implements OrganizationSaver {
    private final OrganizationRepo organizationRepo;
    private final EmployeeSaver employeeSaver;


    @Override
    @Transactional
    public OrganizationEntity create(OrganizationCreateCommand command) {
        OrganizationEntity organization = new OrganizationEntity()
                .setName(command.name().trim())
                .setInn(command.inn())
                .setKpp(command.kpp())
                .setEmail(command.email())
                .setTel(command.tel())
                .setStatus(OrganizationStatus.PENDING);

        inviteFatherEmployee(organization);

        return organizationRepo.save(organization);
    }

    private void inviteFatherEmployee(OrganizationEntity organization) {
        employeeSaver.invite(organization.getEmail(), organization);
    }
}
