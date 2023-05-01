package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.entity.UserEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.model.in.command.employee.EmployeeInviteCommand;
import com.muravev.samokatimmonolit.repo.EmployeeRepo;
import com.muravev.samokatimmonolit.repo.UserRepo;
import com.muravev.samokatimmonolit.service.EmployeeSaver;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeSaverImpl implements EmployeeSaver {
    private final UserRepo userRepo;
    private final EmployeeRepo employeeRepo;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;


    @Override
    @Transactional
    public EmployeeEntity inviteColleague(EmployeeInviteCommand command) {
        EmployeeEntity currentEmployee = securityService.getCurrentEmployee();
        OrganizationEntity organization = currentEmployee.getOrganization();
        return invite(command.email(), organization);
    }

    @Override
    @Transactional
    public EmployeeEntity invite(String email, OrganizationEntity organization) {
        Optional<UserEntity> byEmail = userRepo.findByEmail(email.toLowerCase());
        if (byEmail.isPresent()) {
            throw new ApiException(StatusCode.USER_ALREADY_EXIST);
        }

        String tempPass = generateTempPass();

        EmployeeEntity employee = new EmployeeEntity();

        employee.setOrganization(organization)
                .setEmail(email.toLowerCase())
                .setEncodedPassword(passwordEncoder.encode(tempPass))
                .setNotConfirmed(true);

        userRepo.save(employee);
        log.info("Created employee {}", employee.getEmail());
        return employee;
    }

    private String generateTempPass() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    @Transactional
    public void delete(long id) {
        EmployeeEntity employee = employeeRepo.findById(id)
                .orElseThrow(() -> new ApiException(StatusCode.USER_NOT_FOUND));
        if (employee.isNotConfirmed()) {
            userRepo.delete(employee);
            return;
        }
        employee.setRetired(true)
                .setRetiredAt(ZonedDateTime.now());
    }
}
