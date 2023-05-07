package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.model.in.MapViewIn;

import java.util.Collection;

public interface RentReader {
    Collection<RentEntity> findAllInViewAndRentedByMe(MapViewIn view);
}
