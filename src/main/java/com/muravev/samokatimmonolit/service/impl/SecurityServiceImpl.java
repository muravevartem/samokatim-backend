package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.ClientEntity;
import com.muravev.samokatimmonolit.entity.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.UserEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.repo.UserRepo;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityServiceImpl implements SecurityService {
    private final UserRepo userRepo;


    @Override
    public Optional<UserEntity> getOptCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null)
            return Optional.empty();

        String email = authentication.getPrincipal().toString();
        return userRepo.findByEmail(email.toLowerCase());
    }

    @Override
    public UserEntity getCurrentUser() {
        return getOptCurrentUser()
                .orElseThrow(() -> new ApiException(StatusCode.FORBIDDEN));
    }

    @Override
    public EmployeeEntity getCurrentEmployee() {
        UserEntity currentUser = Hibernate.unproxy(getCurrentUser(), UserEntity.class);
        if (currentUser instanceof EmployeeEntity employee) {
            return employee;
        }
        throw new ApiException(StatusCode.FORBIDDEN);
    }

    @Override
    public ClientEntity getCurrentClient() {
        UserEntity currentUser = Hibernate.unproxy(getCurrentUser(), UserEntity.class);
        if (currentUser instanceof ClientEntity client) {
            return client;
        }
        throw new ApiException(StatusCode.FORBIDDEN);
    }


}
