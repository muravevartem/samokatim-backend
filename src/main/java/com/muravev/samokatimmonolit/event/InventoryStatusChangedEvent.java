package com.muravev.samokatimmonolit.event;

import com.muravev.samokatimmonolit.entity.InventoryEntity;
import com.muravev.samokatimmonolit.model.InventoryStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@DiscriminatorValue(InventoryStatusChangedEvent.TYPE)
@Getter
@Setter
public class InventoryStatusChangedEvent extends AbstractInventoryEvent {
    public static final String TYPE = "INVENTORY_STATUS_CHANGED";

    @JdbcTypeCode(SqlTypes.JSON)
    private Body body;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body {
        private InventoryStatus newValue;
    }


    public static InventoryStatusChangedEvent of(InventoryEntity inventory, InventoryStatus newStatus) {
        InventoryStatusChangedEvent event = new InventoryStatusChangedEvent();
        event.setInventory(inventory);
        event.setBody(new Body(newStatus));
        return event;
    }
}
