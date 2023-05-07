package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.model.in.MapViewIn;
import com.muravev.samokatimmonolit.model.out.MapViewOut;

public interface MapService {
    MapViewOut getClientView(MapViewIn viewIn);
}
