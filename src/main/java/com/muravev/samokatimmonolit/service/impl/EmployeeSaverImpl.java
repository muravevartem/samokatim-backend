package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.user.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.entity.user.UserEntity;
import com.muravev.samokatimmonolit.entity.UserInviteEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.kafka.producer.EmployeeEmailProducer;
import com.muravev.samokatimmonolit.model.in.command.employee.EmployeeInviteCommand;
import com.muravev.samokatimmonolit.model.in.command.employee.EmployeeUpdateCommand;
import com.muravev.samokatimmonolit.repo.EmployeeRepo;
import com.muravev.samokatimmonolit.repo.UserRepo;
import com.muravev.samokatimmonolit.service.EmployeeSaver;
import com.muravev.samokatimmonolit.service.SecurityService;
import com.muravev.samokatimmonolit.service.UserInviter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeSaverImpl implements EmployeeSaver {
    private final UserRepo userRepo;
    private final EmployeeRepo employeeRepo;
    private final SecurityService securityService;
    private final UserInviter userInviter;

    private final EmployeeEmailProducer emailProducer;


    @Override
    @Transactional
    public EmployeeEntity inviteColleague(EmployeeInviteCommand command) {
        EmployeeEntity currentEmployee = securityService.getCurrentEmployee();
        OrganizationEntity organization = currentEmployee.getOrganization();
        return invite(command, organization);
    }

    @Override
    @Transactional
    public EmployeeEntity invite(EmployeeInviteCommand command, OrganizationEntity organization) {
        Optional<UserEntity> byEmail = userRepo.findByEmail(command.email().toLowerCase());
        if (byEmail.isPresent()) {
            throw new ApiException(StatusCode.USER_ALREADY_EXIST);
        }

        EmployeeEntity employee = new EmployeeEntity();

        employee.setOrganization(organization)
                .setEmail(command.email().toLowerCase())
                .setFirstName(command.firstName())
                .setLastName(command.lastName())
                .setNotConfirmed(true);
        EmployeeEntity savedEmployee = userRepo.save(employee);
        UserInviteEntity invite = userInviter.invite(savedEmployee, Duration.ofMinutes(15));
        emailProducer.sendInvite(invite);

        log.info("Created employee {}", employee.getEmail());
        return employee;
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

    @Override
    @Transactional
    public EmployeeEntity update(long id, EmployeeUpdateCommand command) {
        EmployeeEntity employee = employeeRepo.findById(id)
                .orElseThrow(() -> new ApiException(StatusCode.USER_NOT_FOUND));
        employee.setEmail(command.email().trim())
                .setLastName(command.lastName().trim())
                .setFirstName(command.firstName().trim());
        return employee;
    }
}
