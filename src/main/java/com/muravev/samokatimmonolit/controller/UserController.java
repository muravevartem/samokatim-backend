package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.model.in.command.user.UserResetPasswordCommand;
import com.muravev.samokatimmonolit.service.UserWriter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserWriter userWriter;


    @PutMapping("/reset-password")
    public void resetPassword(@RequestBody @Valid UserResetPasswordCommand command) {
        userWriter.resetPassword(command);
    }
}
