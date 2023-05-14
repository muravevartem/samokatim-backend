package com.muravev.samokatimmonolit.report.enity;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.entity.RentEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Immutable
@Subselect("SELECT gen_random_uuid() as id, rent_financial_stat.* FROM rent_financial_stat")
@Getter
@Setter
public class RentStatEntityView {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "date", updatable = false, insertable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_org_id", updatable = false, insertable = false)
    private OrganizationEntity organization;

    @Column(name = "rent_amount", updatable = false, insertable = false)
    private Long amount;

    @Column(name = "rent_money_sum", updatable = false, insertable = false)
    private BigDecimal moneySum;

    @Column(name = "rent_money_avg", updatable = false, insertable = false)
    private BigDecimal moneyAvg;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RentStatEntityView that = (RentStatEntityView) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
