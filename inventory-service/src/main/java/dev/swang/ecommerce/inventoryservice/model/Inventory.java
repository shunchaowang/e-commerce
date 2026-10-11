package dev.swang.ecommerce.inventoryservice.model;

import java.util.UUID;

/**
 * This is the domain model we want to have managed mutability. What does managed mutability mean?
 * Actually, when the object is created, it should be immutable. But we allow some properties to be
 * changed in a manageable manner. Like an inventory, you want to change the quantity and active, we
 * don't want to call that a setQuantity, but we would like to have something with a meaningful
 * name, like a changeQuantity and toggleActive.
 */
public class Inventory {

    private final UUID id;
    private final String sku;
    private final Integer location;
    private final InventoryType type;
    private Integer quantity;

    private Inventory(Builder builder) {
        this.id = builder.id;
        this.sku = builder.sku;
        this.location = builder.location;
        this.type = builder.type;
        this.quantity = builder.quantity;
    }

    public UUID getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getSku() {
        return sku;
    }

    public Integer getLocation() {
        return location;
    }

    public InventoryType getType() {
        return type;
    }

    public void changeQuantity(Integer quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public static class Builder {
        private UUID id;
        private String sku;
        private Integer location;
        private InventoryType type;
        private Integer quantity;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder sku(String sku) {
            this.sku = sku;
            return this;
        }

        public Builder location(Integer location) {
            this.location = location;
            return this;
        }

        public Builder type(InventoryType type) {
            this.type = type;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Inventory build() {
            return new Inventory(this);
        }
    }

}
