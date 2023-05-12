package com.muravev.samokatimmonolit.integration.yookassa.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum PaymentConfirmationType {
    EMBEDDED("embedded"),
    REDIRECT("redirect");


    private final String value;

    @JsonCreator
    public static PaymentConfirmationType from(String value) {
        return Arrays.stream(PaymentConfirmationType.values())
                .filter(x-> Objects.equals(x.value, value))
                .findFirst()
                .orElse(null);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
