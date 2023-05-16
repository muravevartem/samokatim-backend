package com.muravev.samokatimmonolit.model.in.command.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmployeeUpdateCommand(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Email
        String email
) {
}
