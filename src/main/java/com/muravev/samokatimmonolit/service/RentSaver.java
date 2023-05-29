package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.model.in.command.rent.RentCreateCommand;
import com.muravev.samokatimmonolit.model.in.command.rent.RentStopCommand;
import com.muravev.samokatimmonolit.model.out.PaymentOptionsOut;

public interface RentSaver {
    PaymentOptionsOut start(RentCreateCommand command);

    PaymentOptionsOut end(long id);

    PaymentOptionsOut repay(long id);

    void stop(long id, RentStopCommand command);
}
