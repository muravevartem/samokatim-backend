package com.muravev.samokatimmonolit.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Where;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.*;

@Entity
@Table(name = "office")
@Getter
@Setter
@Accessors(chain = true)
public class OfficeEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String alias;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;

    private String address;

    private String postalCode;

    private String timezone;

    private String fullAddress;

    @Column(nullable = false)
    private int capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private OrganizationEntity organization;

    @OneToMany(mappedBy = "office")
    @Where(clause = "status = 'PENDING'")
    private Set<InventoryEntity> inventories = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "office_schedule",
            joinColumns = @JoinColumn(name = "office_id", nullable = false)
    )
    @OrderBy("day")
    private List<OfficeScheduleEmbeddable> schedules = new ArrayList<>();

    public boolean isClosed() {
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OfficeEntity that = (OfficeEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
