package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.ClientEntity;
import com.muravev.samokatimmonolit.model.in.command.client.*;

public interface ClientSaver {
    void invite(ClientInviteCommand command);
    ClientEntity change(ClientChangeEmailCommand command);
    ClientEntity change(ClientChangeFirstNameCommand command);
    ClientEntity change(ClientChangeLastNameCommand command);
    ClientEntity change(ClientChangeTelCommand command);
}
