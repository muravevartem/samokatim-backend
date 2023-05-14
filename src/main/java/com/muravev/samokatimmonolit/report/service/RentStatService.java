package com.muravev.samokatimmonolit.report.service;

import com.muravev.samokatimmonolit.entity.EmployeeEntity;
import com.muravev.samokatimmonolit.report.enity.RentStatEntityView;
import com.muravev.samokatimmonolit.report.repo.RentStatRepo;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentStatService {
    private final RentStatRepo repo;
    private final SecurityService securityService;

    public List<RentStatEntityView> getByMyOrganization(ZonedDateTime start, ZonedDateTime end) {
        EmployeeEntity employee = securityService.getCurrentEmployee();
        return repo.findAllByOrganization(employee.getOrganization(), start.toLocalDate(), end.toLocalDate());
    }
}
