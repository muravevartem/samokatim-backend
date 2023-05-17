package com.muravev.samokatimmonolit.controller.admin;

import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.mapper.RentMapper;
import com.muravev.samokatimmonolit.model.out.RentOut;
import com.muravev.samokatimmonolit.service.RentReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class RentAdminController {
    private final RentReader rentReader;
    private final RentMapper rentMapper;


    @GetMapping(value = "/orgs/{id}/rents", params = {"page", "size"})
    public Page<RentOut> findAll(@PathVariable long id, Pageable pageable) {
        return rentReader.findAll(id, pageable)
                .map(rentMapper::toDto);
    }

    @GetMapping("/rents/{id}")
    public RentOut findOne(@PathVariable long id) {
        RentEntity rent = rentReader.findById(id);
        return rentMapper.toDto(rent);
    }
}
