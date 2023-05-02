package com.muravev.samokatimmonolit.entity;

import com.muravev.samokatimmonolit.event.AbstractInventoryEvent;
import com.muravev.samokatimmonolit.model.InventoryClass;
import com.muravev.samokatimmonolit.model.InventoryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.JoinFormula;

import java.util.*;

@Entity
@Table(name = "inventory")
@Getter
@Setter
public class InventoryEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String alias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private InventoryModelEntity model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    private OrganizationEntity organization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryClass inventoryClass;

    @OneToMany(mappedBy = "inventory")
    @OrderBy("createdAt asc")
    private List<InventoryMonitoringEntity> monitoringRecord = new ArrayList<>();

    @OneToMany(mappedBy = "inventory")
    private SortedSet<AbstractInventoryEvent> events = new TreeSet<>();

    @OneToMany(mappedBy = "inventory")
    private Set<RentEntity> rents = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinFormula("""
                (SELECT rent.id FROM rent
                WHERE rent.end_time IS NULL AND rent.inventory_id = id
                ORDER BY rent.start_time DESC
                LIMIT 1)
            """)
    private RentEntity activeRent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinFormula("""
                (SELECT inventory_monitoring.id FROM inventory_monitoring
                WHERE inventory_monitoring.inventory_id = id
                ORDER BY inventory_monitoring.created_at DESC
                LIMIT 1)
            """)
    private InventoryMonitoringEntity lastMonitoringRecord;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InventoryEntity that = (InventoryEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
