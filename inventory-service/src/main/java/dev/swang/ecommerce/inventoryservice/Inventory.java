package dev.swang.ecommerce.inventoryservice;

public class Inventory {

  private Long id;
  private String skuCode;
  private Integer quantity;

  public Inventory(Long id, String skuCode, Integer quantity) {
    this.skuCode = skuCode;
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

  public void changeQuantity(Integer quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative");
    }
    this.quantity = quantity;
  }
}
