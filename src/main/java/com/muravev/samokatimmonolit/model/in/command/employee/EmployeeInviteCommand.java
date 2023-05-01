package com.muravev.samokatimmonolit.model.in.command.employee;

import jakarta.validation.constraints.Email;

public record EmployeeInviteCommand(
        @Email
        String email
) {
}
