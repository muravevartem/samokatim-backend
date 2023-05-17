package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.user.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.OfficeEntity;
import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.model.in.MapViewIn;
import com.muravev.samokatimmonolit.repo.OfficeRepo;
import com.muravev.samokatimmonolit.service.OfficeReader;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfficeReaderImpl implements OfficeReader {
    private final OfficeRepo officeRepo;
    private final SecurityService securityService;


    @Override
    @Transactional(readOnly = true)
    public Collection<OfficeEntity> findAllInView(MapViewIn view) {
        return officeRepo.findAllByView(view.getNorthEast().lat(),
                view.getNorthEast().lng(),
                view.getSouthWest().lat(),
                view.getSouthWest().lng());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<OfficeEntity> findMyAllInView(MapViewIn view) {
        EmployeeEntity employee = securityService.getCurrentEmployee();
        OrganizationEntity organization = employee.getOrganization();
        return officeRepo.findAllByView(view.getNorthEast().lat(),
                view.getNorthEast().lng(),
                view.getSouthWest().lat(),
                view.getSouthWest().lng(),
                organization);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OfficeEntity> getAllMyOffices(Pageable pageable) {
        EmployeeEntity employee = securityService.getCurrentEmployee();
        OrganizationEntity organization = employee.getOrganization();
        return officeRepo.findAllByOrganization(organization, pageable);
    }

    @Override
    public Page<OfficeEntity> getAllMyOffices(String keyword, Pageable pageable) {
        EmployeeEntity employee = securityService.getCurrentEmployee();
        OrganizationEntity organization = employee.getOrganization();
        return officeRepo.findAllByOrganizationAndKeyword(keyword, organization, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public OfficeEntity getOneMy(long id) {
        EmployeeEntity employee = securityService.getCurrentEmployee();
        OrganizationEntity organization = employee.getOrganization();
        return officeRepo.findAllByIdAndOrganization(id, organization)
                .orElseThrow(() -> new ApiException(StatusCode.OFFICE_NOT_FOUND));
    }
}
