package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.user.AdminEntity;
import com.muravev.samokatimmonolit.entity.user.ClientEntity;
import com.muravev.samokatimmonolit.entity.user.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.user.UserEntity;

import java.util.Optional;

public interface SecurityService {
    Optional<UserEntity> getOptCurrentUser();

    UserEntity getCurrentUser();

    EmployeeEntity getCurrentEmployee();

    ClientEntity getCurrentClient();

    AdminEntity getCurrentAdmin();
}
