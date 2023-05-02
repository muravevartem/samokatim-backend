package com.muravev.samokatimmonolit.service;


import com.muravev.samokatimmonolit.model.in.AuthIn;
import com.muravev.samokatimmonolit.model.out.AuthOut;

public interface AuthService {
    AuthOut auth(AuthIn auth);
}
