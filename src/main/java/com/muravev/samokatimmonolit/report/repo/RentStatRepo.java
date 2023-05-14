package com.muravev.samokatimmonolit.report.repo;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.report.enity.RentStatEntityView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface RentStatRepo extends Repository<RentStatEntityView, UUID> {

    @Query("""
            SELECT stat FROM RentStatEntityView stat
            WHERE stat.organization = :organization
                    AND stat.date BETWEEN :start AND :end
            """)
    List<RentStatEntityView> findAllByOrganization(OrganizationEntity organization, LocalDate start, LocalDate end);
}
