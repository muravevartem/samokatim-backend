package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
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
}
