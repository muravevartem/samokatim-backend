package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.entity.user.ClientEntity;
import com.muravev.samokatimmonolit.mapper.UserMapper;
import com.muravev.samokatimmonolit.model.in.command.client.*;
import com.muravev.samokatimmonolit.model.out.ClientOut;
import com.muravev.samokatimmonolit.service.ClientReader;
import com.muravev.samokatimmonolit.service.ClientSaver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientSaver clientSaver;
    private final ClientReader clientReader;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ClientOut me() {
        ClientEntity client = clientReader.me();
        return userMapper.toDto(client);
    }

    @PutMapping("/me/firstname")
    public ClientOut change(@Valid @RequestBody ClientChangeFirstNameCommand command) {
        ClientEntity changed = clientSaver.change(command);
        return userMapper.toDto(changed);
    }

    @PutMapping("/me/lastname")
    public ClientOut change(@Valid @RequestBody ClientChangeLastNameCommand command) {
        ClientEntity changed = clientSaver.change(command);
        return userMapper.toDto(changed);
    }

    @PutMapping("/me/email")
    public ClientOut change(@Valid @RequestBody ClientChangeEmailCommand command) {
        ClientEntity changed = clientSaver.change(command);
        return userMapper.toDto(changed);
    }

    @PutMapping("/me/tel")
    public ClientOut change(@Valid @RequestBody ClientChangeTelCommand command) {
        ClientEntity changed = clientSaver.change(command);
        return userMapper.toDto(changed);
    }

    @PostMapping
    public void invite(@Valid @RequestBody ClientInviteCommand command) {
        clientSaver.invite(command);
    }


}
