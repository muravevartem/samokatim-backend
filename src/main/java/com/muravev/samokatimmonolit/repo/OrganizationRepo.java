package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrganizationRepo extends JpaRepository<OrganizationEntity, Long> {
    Optional<OrganizationEntity> findByInn(String inn);

    @Query("""
            SELECT sum(payment.price) FROM PaymentEntity payment
            WHERE payment.rent.inventory.organization = :organization
                    AND payment.status = 'COMPLETED'
            """)
    Optional<Double> getRevenue(OrganizationEntity organization);

    @Query("""
            SELECT org FROM OrganizationEntity org
            WHERE LOWER(org.name) LIKE CONCAT('%', LOWER(:keyword), '%')
            """)
    Page<OrganizationEntity> findAllByKeyword(String keyword, Pageable pageable);
}
