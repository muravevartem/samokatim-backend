package com.muravev.samokatimmonolit.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InventoryType {
    BICYCLE("BK-"),
    BICYCLE_EL("BKE-"),
    SCOOTER("SC-"),
    SCOOTER_EL("SCE-");

    private final String prefix;
}
