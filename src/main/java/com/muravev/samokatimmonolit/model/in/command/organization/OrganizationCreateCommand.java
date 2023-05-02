package com.muravev.samokatimmonolit.model.in.command.organization;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OrganizationCreateCommand(
        @NotBlank
        String inn,
        @Email
        String email,
        @NotBlank
        String tel
) {
}
