package com.muravev.samokatimmonolit.model.in.command.user;

import jakarta.validation.constraints.Email;

public record UserResetPasswordCommand(
        @Email String email
) {
}
