package com.muravev.samokatimmonolit.event;

import com.muravev.samokatimmonolit.entity.AuditEntity;
import com.muravev.samokatimmonolit.entity.InventoryEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "inventory_event")
@Inheritance
@DiscriminatorColumn(name = "type")
@Getter
@Setter
public abstract class AbstractInventoryEvent extends AuditEntity implements Comparable<AbstractInventoryEvent> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id")
    private InventoryEntity inventory;

    public void setInventory(InventoryEntity inventory) {
        if (inventory != null) {
            this.inventory = inventory;
            this.inventory.getEvents().add(this);
        }
    }

    public abstract Object getBody();

    public abstract String getType();

    @Override
    public int compareTo(AbstractInventoryEvent o) {
        return getCreatedAt().compareTo(o.getCreatedAt());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AbstractInventoryEvent that = (AbstractInventoryEvent) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
