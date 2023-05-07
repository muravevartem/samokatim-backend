package com.muravev.samokatimmonolit.model.out;

import java.time.LocalDate;

public record ClientOut(
        Long id,
        String firstName,
        String lastName,
        String email,
        boolean notConfirmed,
        LocalDate birthDate,
        String tel
) {
}
