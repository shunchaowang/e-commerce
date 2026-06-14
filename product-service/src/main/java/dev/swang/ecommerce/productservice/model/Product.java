package dev.swang.ecommerce.productservice.model;

import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {

  @Id
  private String id;
  private String name;
  private String description;
  private BigDecimal price;

  public Product() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Product product = (Product) o;
    return price.compareTo(product.price) == 0 && Objects.equals(id,
        product.id) && Objects.equals(name, product.name) && Objects.equals(
        description, product.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, price);
  }

  public static class Builder {

    private final Product product;

    public Builder() {
      product = new Product();
    }

    public Builder name(String name) {
      product.setName(name);
      return this;
    }

    public Builder description(String description) {
      product.setDescription(description);
      return this;
    }

    public Builder price(BigDecimal price) {
      product.setPrice(price);
      return this;
    }

    public Product build() {
      return product;
    }
  }
}
