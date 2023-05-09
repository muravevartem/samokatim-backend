package com.muravev.samokatimmonolit.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "inventory_monitoring")
@Getter
@Setter
@Accessors(chain = true)
public class InventoryMonitoringEntity extends AuditEntity implements Comparable<InventoryMonitoringEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id")
    private InventoryEntity inventory;

    private Double lat;

    private Double lng;

    private Double speed;

    private Integer satellites;

    private Double altitude;

    private ZonedDateTime originalTimestamp;


    @Override
    public int compareTo(@NotNull InventoryMonitoringEntity o) {
        return getCreatedAt().compareTo(o.getCreatedAt());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InventoryMonitoringEntity that = (InventoryMonitoringEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
