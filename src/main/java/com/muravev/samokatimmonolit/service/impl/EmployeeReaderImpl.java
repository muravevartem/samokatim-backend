package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.user.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.repo.EmployeeRepo;
import com.muravev.samokatimmonolit.repo.OrganizationRepo;
import com.muravev.samokatimmonolit.service.EmployeeReader;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeReaderImpl implements EmployeeReader {
    private final OrganizationRepo organizationRepo;
    private final EmployeeRepo employeeRepo;
    private final SecurityService securityService;

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeEntity> findAllColleagues(String keyword, boolean showRetired, Pageable pageable) {
        EmployeeEntity employee = securityService.getCurrentEmployee();

        OrganizationEntity organization = employee.getOrganization();
        if (keyword == null || keyword.isBlank()) {
            return employeeRepo.findAllByOrganization(organization, showRetired, pageable);
        }
        return employeeRepo.findAllByOrganization(keyword, organization, showRetired, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeEntity findByIdAsEmployee(long id) {
        EmployeeEntity currentEmployee = securityService.getCurrentEmployee();

        return employeeRepo.findById(id)
                .filter(employee -> Objects.equals(currentEmployee.getOrganization(), employee.getOrganization()))
                .orElseThrow(() -> new ApiException(StatusCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeEntity findById(long id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new ApiException(StatusCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeEntity> findAll(long orgId, Pageable pageable) {
        OrganizationEntity organization = organizationRepo.getReferenceById(orgId);
        return employeeRepo.findAllByOrganization(organization, true, pageable);
    }
}
