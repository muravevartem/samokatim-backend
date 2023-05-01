package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.model.in.command.employee.EmployeeInviteCommand;

public interface EmployeeSaver {
    EmployeeEntity inviteColleague(EmployeeInviteCommand command);

    EmployeeEntity invite(String email, OrganizationEntity organization);

    void delete(long id);
}
