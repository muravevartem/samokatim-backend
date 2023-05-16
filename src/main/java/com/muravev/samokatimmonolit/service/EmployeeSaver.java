package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.model.in.command.employee.EmployeeInviteCommand;
import com.muravev.samokatimmonolit.model.in.command.employee.EmployeeUpdateCommand;
import org.springframework.transaction.annotation.Transactional;

public interface EmployeeSaver {
    EmployeeEntity inviteColleague(EmployeeInviteCommand command);

    EmployeeEntity invite(EmployeeInviteCommand command, OrganizationEntity organization);

    void delete(long id);

    EmployeeEntity update(long id, EmployeeUpdateCommand command);
}
