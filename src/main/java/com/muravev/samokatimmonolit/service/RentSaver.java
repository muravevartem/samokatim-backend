package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.model.in.command.rent.RentCreateCommand;

public interface RentSaver {
    RentEntity start(RentCreateCommand command);

    RentEntity end(long id);
}
