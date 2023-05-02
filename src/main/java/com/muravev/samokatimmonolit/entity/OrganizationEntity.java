package com.muravev.samokatimmonolit.entity;

import com.muravev.samokatimmonolit.model.OrganizationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "org")
@Getter
@Setter
@Accessors(chain = true)
public class OrganizationEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String inn;

    private String kpp;

    @Column(nullable = false, unique = true)
    private String email;

    private String tel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrganizationStatus status;

    @OneToMany(mappedBy = "organization")
    private Set<InventoryEntity> inventories = new HashSet<>();

    @OneToMany(mappedBy = "organization")
    private Set<EmployeeEntity> employees = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrganizationEntity that = (OrganizationEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
