package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.OfficeEntity;
import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.repo.OfficeRepo;
import com.muravev.samokatimmonolit.service.OfficeReader;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfficeReaderImpl implements OfficeReader {
    private final OfficeRepo officeRepo;
    private final SecurityService securityService;


    @Override
    @Transactional(readOnly = true)
    public Page<OfficeEntity> getAllMyOffices(Pageable pageable) {
        EmployeeEntity employee = securityService.getCurrentEmployee();
        OrganizationEntity organization = employee.getOrganization();
        return officeRepo.findAllByOrganization(organization, pageable);
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
