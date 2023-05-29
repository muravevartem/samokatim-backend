package com.muravev.samokatimmonolit.model.in.command.rent;

import com.muravev.samokatimmonolit.model.RentStopCause;

public record RentStopCommand(
        RentStopCause cause,
        Long officeId
) {
}
