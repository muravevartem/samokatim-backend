package com.muravev.samokatimmonolit.model.out;

import java.time.LocalDate;

public record EmployeeOut(
        Long id,
        String firstName,
        String lastName,
        OrganizationFullOut organization,
        String email,
        boolean notConfirmed,
        LocalDate birthDate,
        String tel,
        boolean retired

) {
}
