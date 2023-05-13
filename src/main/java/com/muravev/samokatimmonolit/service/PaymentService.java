package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.model.out.PaymentOptionsOut;

public interface PaymentService {
    PaymentOptionsOut pay(RentEntity rent);

    PaymentOptionsOut deposit(RentEntity rent);
}
