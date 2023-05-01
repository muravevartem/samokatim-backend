package com.muravev.samokatimmonolit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Entity
@DiscriminatorValue("EMPLOYEE")
@Getter
@Setter
@Accessors(chain = true)
public class EmployeeEntity extends UserEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    private OrganizationEntity organization;

    private boolean retired;
    private ZonedDateTime retiredAt;

    public EmployeeEntity setOrganization(OrganizationEntity organization) {
        if (organization != null) {
            this.organization = organization;
            this.organization.getEmployees().add(this);
        }
        return this;
    }
}
