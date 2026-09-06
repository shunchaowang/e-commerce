package dev.swang.ecommerce.productservice.web;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(String id, String name, String description, BigDecimal price,
                              Instant createdAt, Instant updatedAt) {

}
