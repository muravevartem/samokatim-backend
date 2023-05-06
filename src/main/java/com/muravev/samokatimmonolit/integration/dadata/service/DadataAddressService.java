package com.muravev.samokatimmonolit.integration.dadata.service;

import com.muravev.samokatimmonolit.model.Address;

import java.util.List;

public interface DadataAddressService {
    List<Address> getFirstByGeoPoint(double lat, double lng);
}
