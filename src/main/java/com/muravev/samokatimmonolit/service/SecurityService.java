package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.ClientEntity;
import com.muravev.samokatimmonolit.entity.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.UserEntity;

public interface SecurityService {
    UserEntity getCurrentUser();

    EmployeeEntity getCurrentEmployee();

    ClientEntity getCurrentClient();
}
