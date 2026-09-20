package dev.swang.ecommerce.inventoryservice.model;

/**
 * This is the domain model we want to have managed mutability. What does managed mutability mean?
 * Actually, when the object is created, it should be immutable. But we allow some properties to be
 * changed in a manageable manner. Like an inventory, you want to change the quantity, we don't want
 * to call that a setQuantity, but we would like to have something with a meaningful name, like a
 * changeQuantity.
 */
public class Inventory {

    private final Long id;
    private final String skuCode;
    private Integer quantity;

    private Inventory(Builder builder) {
        this.id = builder.id;
        this.skuCode = builder.skuCode;
        this.quantity = builder.quantity;
    }

    public Long getId() {
        return id;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void changeQuantity(Integer quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public static class Builder {
        private Long id;
        private String skuCode;
        private Integer quantity;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder skuCode(String skuCode) {
            this.skuCode = skuCode;
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
