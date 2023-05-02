package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.model.in.command.user.UserInviteCompleteCommand;
import com.muravev.samokatimmonolit.service.UserInviter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-invites")
@RequiredArgsConstructor
public class UserInviteController {
    private final UserInviter userInviter;


    @PutMapping("/{id}")
    public void completeInvite(@PathVariable long id, @RequestBody @Valid UserInviteCompleteCommand command) {
        userInviter.completeInvite(id, command);
    }
}
