package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.RentEntity;

public interface PaymentService {
    void pay(RentEntity rent);
}
