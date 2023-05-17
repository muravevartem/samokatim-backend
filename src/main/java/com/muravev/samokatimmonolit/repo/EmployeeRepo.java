package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.user.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepo extends JpaRepository<EmployeeEntity, Long> {

    @Query("""
            SELECT employee FROM EmployeeEntity employee
            WHERE (employee.retired = FALSE OR :showRetired = TRUE)
                    AND employee.organization = :organization
            """)
    Page<EmployeeEntity> findAllByOrganization(OrganizationEntity organization, boolean showRetired, Pageable pageable);

    @Query("""
            SELECT employee FROM EmployeeEntity employee
            WHERE (employee.retired = FALSE OR :showRetired = TRUE)
                    AND employee.organization = :organization
                    AND LOWER(employee.firstName) LIKE LOWER('%'||:keyword||'%')
                    AND LOWER(employee.lastName) LIKE LOWER('%'||:keyword||'%')
                    AND LOWER(employee.email) LIKE LOWER('%'||:keyword||'%')
            """)
    Page<EmployeeEntity> findAllByOrganization(String keyword, OrganizationEntity organization, boolean showRetired, Pageable pageable);

}
