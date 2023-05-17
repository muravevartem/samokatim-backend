package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.user.ClientEntity;
import com.muravev.samokatimmonolit.entity.UserInviteEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.kafka.producer.ClientEmailProducer;
import com.muravev.samokatimmonolit.model.in.command.client.*;
import com.muravev.samokatimmonolit.repo.ClientRepo;
import com.muravev.samokatimmonolit.repo.UserRepo;
import com.muravev.samokatimmonolit.service.ClientSaver;
import com.muravev.samokatimmonolit.service.SecurityService;
import com.muravev.samokatimmonolit.service.UserInviter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static com.muravev.samokatimmonolit.util.StringUtil.normal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientSaverImpl implements ClientSaver {
    private final ClientRepo clientRepo;
    private final UserRepo userRepo;
    private final UserInviter userInviter;
    private final SecurityService securityService;
    private final ClientEmailProducer emailProducer;

    @Override
    @Transactional
    public void invite(ClientInviteCommand command) {
        ClientEntity client = new ClientEntity();
        client.setEmail(command.email());

        ClientEntity savedClient = clientRepo.save(client);
        UserInviteEntity invite = userInviter.invite(savedClient, Duration.ofMinutes(15L));
        emailProducer.sendInvite(invite);
    }

    @Override
    @Transactional
    public ClientEntity change(ClientChangeEmailCommand command) {
        ClientEntity client = securityService.getCurrentClient();
        String email = normal(command.value().toLowerCase());
        userRepo.findByEmail(email)
                        .ifPresent(any->{
                            throw new ApiException(StatusCode.USER_ALREADY_EXIST);
                        });
        client.setEmail(email);
        return client;
    }

    @Override
    @Transactional
    public ClientEntity change(ClientChangeFirstNameCommand command) {
        ClientEntity client = securityService.getCurrentClient();
        client.setFirstName(command.value());
        return client;
    }

    @Override
    @Transactional
    public ClientEntity change(ClientChangeLastNameCommand command) {
        ClientEntity client = securityService.getCurrentClient();
        client.setLastName(command.value());
        return client;
    }

    @Override
    @Transactional
    public ClientEntity change(ClientChangeTelCommand command) {
        ClientEntity client = securityService.getCurrentClient();
        client.setTel(command.value());
        return client;
    }
}
