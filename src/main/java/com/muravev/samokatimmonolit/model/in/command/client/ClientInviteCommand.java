package com.muravev.samokatimmonolit.model.in.command.client;

import jakarta.validation.constraints.Email;

public record ClientInviteCommand(
        @Email
        String email
) {
}
