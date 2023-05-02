package com.muravev.samokatimmonolit.model.in.command.user;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserInviteCompleteCommand(
        @NotBlank
        String password,
        @NotBlank
        @Length(min = 8, max = 8)
        String code
) {
}
