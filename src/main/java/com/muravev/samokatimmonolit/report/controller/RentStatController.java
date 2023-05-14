package com.muravev.samokatimmonolit.report.controller;

import com.muravev.samokatimmonolit.report.mapper.RentStatMapper;
import com.muravev.samokatimmonolit.report.model.out.RentStatOut;
import com.muravev.samokatimmonolit.report.service.RentStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rents/stat")
@RequiredArgsConstructor
public class RentStatController {
    private final RentStatService rentStatService;

    private final RentStatMapper mapper;


    @GetMapping(params = {"my" , "start", "end"})
    public List<RentStatOut> getMyOrganization(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam ZonedDateTime start,
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam ZonedDateTime end) {
        return rentStatService.getByMyOrganization(start, end).stream()
                .map(mapper::toDto)
                .toList();
    }
}
