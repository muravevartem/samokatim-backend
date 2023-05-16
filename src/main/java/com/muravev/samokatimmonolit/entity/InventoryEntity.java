package com.muravev.samokatimmonolit.entity;

import com.muravev.samokatimmonolit.event.AbstractInventoryEvent;
import com.muravev.samokatimmonolit.model.InventoryClass;
import com.muravev.samokatimmonolit.model.InventoryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.Where;

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

    @Column(unique = true, nullable = false)
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

    @Column(nullable = false)
    private boolean supportsTelemetry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_id")
    private OfficeEntity office;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL)
    @OrderBy("createdAt asc")
    private List<InventoryMonitoringEntity> monitoringRecord = new ArrayList<>();

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL)
    private SortedSet<AbstractInventoryEvent> events = new TreeSet<>();

    @OneToMany(mappedBy = "inventory")
    private Set<RentEntity> rents = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "inventory_tariff",
            joinColumns = @JoinColumn(name = "inventory_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "tariff_id", nullable = false))
    @Where(clause = "deleted_at is null")
    private SortedSet<OrganizationTariffEntity> tariffs = new TreeSet<>();

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

    public void setOffice(OfficeEntity office) {
        if (this.office != null) {
            this.office.getInventories().remove(this);
        }
        this.office = office;
        if (this.office != null) {
            this.office.getInventories().add(this);
        }
    }

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
