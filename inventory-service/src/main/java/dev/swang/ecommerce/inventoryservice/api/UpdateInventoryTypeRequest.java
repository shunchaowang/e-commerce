package dev.swang.ecommerce.inventoryservice.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record UpdateInventoryTypeRequest(@NotBlank(message = "sku is required") String sku,
        @Positive(message = "location must be a positive number") Integer location,
        @Pattern(regexp = "^(STORE|DC|EFC)$",
                message = "type must be either STORE, DC or EFC") String type) {

}
