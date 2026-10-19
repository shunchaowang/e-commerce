package dev.swang.ecommerce.inventoryservice.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

// tyoe can be STORE, DC, EFC
public record CreateInventoryRequest(@NotBlank(message = "sku is required") String sku,
        @Positive(message = "location must be a positive number") Integer location,
        @Min(value = 1, message = "quantity must be greater than or equal to 1") @Max(value = 1000,
                message = "quantity must be less than or equal to 1000") Integer quantity,
        @Pattern(regexp = "^(STORE|DC|EFC)$",
                message = "type must be either STORE, DC or EFC") String type) {

}
