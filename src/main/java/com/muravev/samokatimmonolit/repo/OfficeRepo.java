package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.OfficeEntity;
import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OfficeRepo extends JpaRepository<OfficeEntity, Long> {
    Page<OfficeEntity> findAllByOrganization(OrganizationEntity organization, Pageable pageable);

    @Query("""
            SELECT office FROM OfficeEntity office
            WHERE office.organization = :organization
                AND LOWER(office.alias) LIKE LOWER(CONCAT('%', :keyword,'%'))
            """)
    Page<OfficeEntity> findAllByOrganizationAndKeyword(String keyword, OrganizationEntity organization, Pageable pageable);

    Optional<OfficeEntity> findAllByIdAndOrganization(Long id, OrganizationEntity organization);

    @Query("""
            SELECT DISTINCT office FROM OfficeEntity office
            WHERE office.lng BETWEEN :lngSW AND :lngNE
                AND
                office.lat BETWEEN :latSW AND :latNE
            ORDER BY office.id DESC
            """)
    List<OfficeEntity> findAllByView(@Param("latNE") double latNE,
                                              @Param("lngNE") double lngNE,
                                              @Param("latSW") double latSW,
                                              @Param("lngSW") double lngSW);

    @Query("""
            SELECT DISTINCT office FROM OfficeEntity office
            WHERE office.lng BETWEEN :lngSW AND :lngNE
                AND
                office.lat BETWEEN :latSW AND :latNE
                AND
                office.organization = :organization
            ORDER BY office.id DESC
            """)
    List<OfficeEntity> findAllByView(@Param("latNE") double latNE,
                                     @Param("lngNE") double lngNE,
                                     @Param("latSW") double latSW,
                                     @Param("lngSW") double lngSW,
                                     OrganizationEntity organization);
}
