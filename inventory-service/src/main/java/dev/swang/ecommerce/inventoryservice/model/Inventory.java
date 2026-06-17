package dev.swang.ecommerce.inventoryservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "inventory")
public class Inventory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String skuCode;
  private Integer quantity;

  @CreatedDate
  private Instant createdAt;

  @LastModifiedDate
  private Instant updatedAt;

  public Inventory() {
  }

  public void setSkuCode(String skuCode) {
    this.skuCode = skuCode;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
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

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Inventory inventory = (Inventory) o;
    return Objects.equals(id, inventory.id) && Objects.equals(skuCode,
        inventory.skuCode) && Objects.equals(quantity, inventory.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, skuCode, quantity);
  }

  public static class Builder {

    private final Inventory inventory;

    public Builder() {
      inventory = new Inventory();
    }

    public Inventory build() {
      return inventory;
    }

    public Builder skuCode(String skuCode) {
      inventory.skuCode = skuCode;
      return this;
    }

    public Builder quantity(Integer quantity) {
      inventory.quantity = quantity;
      return this;
    }
  }
}
