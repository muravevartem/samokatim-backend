package com.muravev.samokatimmonolit.entity;

import com.muravev.samokatimmonolit.model.DepositStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "deposit")
@Getter
@Setter
@Accessors(chain = true)
public class DepositEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "rent_id", nullable = false)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "rent_id")
    private RentEntity rent;

    private BigDecimal price;

    private String url;

    private String bankId;

    private String refundBankId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DepositStatus status = DepositStatus.CREATING;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DepositEntity that = (DepositEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
