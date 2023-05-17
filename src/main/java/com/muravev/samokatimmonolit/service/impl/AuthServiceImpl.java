package com.muravev.samokatimmonolit.service.impl;


import com.muravev.samokatimmonolit.entity.user.ClientEntity;
import com.muravev.samokatimmonolit.entity.user.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.user.UserEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.model.UserRole;
import com.muravev.samokatimmonolit.model.in.AuthIn;
import com.muravev.samokatimmonolit.model.out.AuthOut;
import com.muravev.samokatimmonolit.repo.UserRepo;
import com.muravev.samokatimmonolit.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${auth.secret}")
    private String secret;


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public AuthOut auth(AuthIn auth) {
        UserEntity person = userRepo.findByEmail(auth.username().toLowerCase())
                .orElseThrow(() -> new ApiException(StatusCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(auth.password(), person.getEncodedPassword()))
            throw new ApiException(StatusCode.USER_NOT_FOUND);

        String token = generateAccessToken(person);
        return new AuthOut(token);
    }

    private UserRole extractRoles(UserEntity user) {
        if (user instanceof EmployeeEntity)
            return UserRole.LOCAL_ADMIN;
        if (user instanceof ClientEntity)
            return UserRole.CLIENT;
        return UserRole.GLOBAL_ADMIN;
    }

    private String generateAccessToken(UserEntity user) {
        return Jwts.builder()
                .setIssuer("samokatim")
                .setSubject("user")
                .claim("username", user.getEmail())
                .claim("tel", user.getTel())
                .claim("email", user.getEmail())
                .claim("roles", List.of(extractRoles(user).name()))
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.ofEpochSecond(4622470422L)))
                .signWith(
                        SignatureAlgorithm.HS256,
                        secret.getBytes(StandardCharsets.UTF_8)
                )
                .compact();
    }
}
