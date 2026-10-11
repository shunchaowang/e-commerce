package dev.swang.ecommerce.inventoryservice.persistence;

import java.util.Objects;
import java.util.UUID;

import dev.swang.ecommerce.commonmodel.BaseJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Extends BaseJpaEntity with the UUID, version, createdAt and updatedAt InventoryEntity
 */
@Entity
@Table(name = "inventory")
public class InventoryEntity extends BaseJpaEntity {

    private Integer location;
    private String sku;
    private Integer quantity;
    private String type;

    protected InventoryEntity() {}

    private InventoryEntity(Builder builder) {
        super(builder.id);
        this.sku = builder.sku;
        this.location = builder.location;
        this.quantity = builder.quantity;
        this.type = builder.type;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public static class Builder {

        private UUID id;
        private String sku;
        private Integer location;
        private Integer quantity;
        private String type;

        public Builder(UUID id) {
            this.id = Objects.requireNonNull(id);
        }

        public Builder sku(String sku) {
            this.sku = sku;
            return this;
        }

        public Builder location(Integer location) {
            this.location = location;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return null;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public InventoryEntity build() {
            return new InventoryEntity(this);
        }

    }

}
