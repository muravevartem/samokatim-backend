package com.muravev.samokatimmonolit.entity.user;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import java.time.ZonedDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EmployeeEntity that = (EmployeeEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
