package com.muravev.samokatimmonolit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "cheque")
@Getter
@Setter
@Accessors(chain = true)
public class ChequeEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "rent_id", nullable = false)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "rent_id")
    private RentEntity rent;

    private BigDecimal price;

    private String bankChequeNumber;

    private Boolean paid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChequeEntity that = (ChequeEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
