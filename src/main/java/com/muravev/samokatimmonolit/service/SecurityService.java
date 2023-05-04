package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.ClientEntity;
import com.muravev.samokatimmonolit.entity.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.UserEntity;

import java.util.Optional;

public interface SecurityService {
    Optional<UserEntity> getOptCurrentUser();

    UserEntity getCurrentUser();

    EmployeeEntity getCurrentEmployee();

    ClientEntity getCurrentClient();
}
