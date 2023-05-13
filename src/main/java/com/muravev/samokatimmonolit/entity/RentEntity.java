package com.muravev.samokatimmonolit.entity;

import com.muravev.samokatimmonolit.model.RentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
@Table(name = "rent")
@Getter
@Setter
@Accessors(chain = true)
public class RentEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private InventoryEntity inventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id", nullable = false)
    private OrganizationTariffEntity tariff;

    @Enumerated(EnumType.STRING)
    private RentStatus status;

    @ManyToMany
    @JoinTable(
            name = "rent_track",
            joinColumns = @JoinColumn(name = "rent_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "record_id", nullable = false)
    )
    private SortedSet<InventoryMonitoringEntity> track = new TreeSet<>();

    @OneToOne(mappedBy = "rent", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private PaymentEntity cheque;

    @OneToOne(mappedBy = "rent", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private DepositEntity deposit;

    public void setCheque(PaymentEntity cheque) {
        this.cheque = cheque;
        cheque.setRent(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RentEntity that = (RentEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
