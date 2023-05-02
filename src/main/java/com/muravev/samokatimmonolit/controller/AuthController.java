package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.model.in.AuthIn;
import com.muravev.samokatimmonolit.model.out.AuthOut;
import com.muravev.samokatimmonolit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping
    @PreAuthorize("permitAll()")
    public AuthOut auth(@RequestBody @Valid AuthIn in) {
        return authService.auth(in);
    }
}
