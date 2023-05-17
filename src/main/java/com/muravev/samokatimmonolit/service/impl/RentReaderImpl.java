package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.*;
import com.muravev.samokatimmonolit.entity.user.ClientEntity;
import com.muravev.samokatimmonolit.entity.user.EmployeeEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.model.in.MapViewIn;
import com.muravev.samokatimmonolit.repo.InventoryMonitoringRepo;
import com.muravev.samokatimmonolit.repo.RentRepo;
import com.muravev.samokatimmonolit.service.RentReader;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentReaderImpl implements RentReader {
    private final RentRepo rentRepo;
    private final InventoryMonitoringRepo monitoringRepo;
    private final SecurityService securityService;


    @Override
    @Transactional(readOnly = true)
    public Collection<RentEntity> findAllInViewAndRentedByMe(MapViewIn view) {
        ClientEntity currentClient = securityService.getCurrentClient();

        var northEast = view.getNorthEast();
        var southWest = view.getSouthWest();
        log.info("[GEO] getting location ne-lat: {} ne-lng: {} sw-lat: {} sw-lng: {}",
                northEast.lat(), northEast.lng(), southWest.lat(), southWest.lng());
        return rentRepo.findAllByView(
                northEast.lat(),
                northEast.lng(),
                southWest.lat(),
                southWest.lng(),
                currentClient
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RentEntity> findAllCompletedByClient(ClientEntity client, Pageable pageable) {
        return rentRepo.findAllCompletedRents(client, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RentEntity> findMyAll(Pageable pageable) {
        ClientEntity currentClient = securityService.getCurrentClient();
        return findAllCompletedByClient(currentClient, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentEntity> findMyActiveAll() {
        ClientEntity currentClient = securityService.getCurrentClient();
        return rentRepo.findAllActiveByClient(currentClient);
    }

    @Override
    @Transactional(readOnly = true)
    public RentEntity findMyById(long id) {
        ClientEntity currentClient = securityService.getCurrentClient();
        RentEntity rent = rentRepo.findByIdAndClient(id, currentClient)
                .orElseThrow(() -> new ApiException(StatusCode.RENT_NOT_FOUND));
        return rent;
    }

    @Override
    @Transactional(readOnly = true)
    public SortedSet<InventoryMonitoringEntity> getTrack(long id) {
        ClientEntity currentClient = securityService.getCurrentClient();
        RentEntity rent = rentRepo.findByIdAndClient(id, currentClient)
                .orElseThrow(() -> new ApiException(StatusCode.RENT_NOT_FOUND));
        List<InventoryMonitoringEntity> track = monitoringRepo.findAllByInventoryAndTime(
                rent.getInventory(),
                rent.getStartTime(),
                rent.getEndTime() == null
                        ? ZonedDateTime.now()
                        : rent.getEndTime()
        );
        return new TreeSet<>(track);
    }

    @Override
    public Page<RentEntity> findAll(long orgId, Pageable pageable) {
        return rentRepo.findAllByOrganization(new OrganizationEntity().setId(orgId), pageable);
    }

    @Override
    public Page<RentEntity> findMyOrgAll(Pageable pageable) {
        EmployeeEntity employee = securityService.getCurrentEmployee();
        OrganizationEntity organization = employee.getOrganization();
        return rentRepo.findAllByOrganization(organization, pageable);
    }

    @Override
    public RentEntity findById(long id) {
        return rentRepo.findById(id)
                .orElseThrow(() -> new ApiException(StatusCode.RENT_NOT_FOUND));
    }

}
