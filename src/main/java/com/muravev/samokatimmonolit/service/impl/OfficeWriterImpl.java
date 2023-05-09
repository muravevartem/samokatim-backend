package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.*;
import com.muravev.samokatimmonolit.integration.dadata.service.DadataAddressService;
import com.muravev.samokatimmonolit.model.Address;
import com.muravev.samokatimmonolit.model.in.GeoPointIn;
import com.muravev.samokatimmonolit.model.in.command.office.OfficeCreateCommand;
import com.muravev.samokatimmonolit.repo.InventoryRepo;
import com.muravev.samokatimmonolit.repo.OfficeRepo;
import com.muravev.samokatimmonolit.service.OfficeWriter;
import com.muravev.samokatimmonolit.service.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfficeWriterImpl implements OfficeWriter {
    private final OfficeRepo officeRepo;
    private final InventoryRepo inventoryRepo;

    private final DadataAddressService addressService;
    private final SecurityService securityService;


    @Override
    @Transactional
    public OfficeEntity create(@RequestBody @Valid OfficeCreateCommand command) {
        EmployeeEntity employee = securityService.getCurrentEmployee();
        OrganizationEntity organization = employee.getOrganization();

        List<OfficeScheduleEmbeddable> schedules = command.schedules().stream()
                .map(dto -> new OfficeScheduleEmbeddable()
                        .setDay(dto.day())
                        .setStart(dto.start())
                        .setEnd(dto.end())
                        .setDayOff(dto.dayOff()))
                .toList();

        GeoPointIn location = command.location();
        List<Address> addresses = addressService.getFirstByGeoPoint(location.lat(), location.lng());

        Address nearest = addresses.stream()
                .findFirst()
                .orElseGet(() -> new Address(null));

        OfficeEntity office = new OfficeEntity()
                .setLat(command.location().lat())
                .setLng(command.location().lng())
                .setAlias(command.alias())
                .setCapacity(command.capacity())
                .setOrganization(organization)
                .setAddress(nearest.value())
                .setSchedules(schedules);

        return officeRepo.save(office);
    }

}
