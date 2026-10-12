package dev.swang.ecommerce.inventoryservice.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class InventoryEntityTest {

    @Test
    void builder_WhenAllFieldsAreProvided_ThenBuildsEntityWithThoseValues() {
        UUID id = UUID.randomUUID();
        String sku = "SKU123";
        Integer location = 899;
        Integer quantity = 10;
        String type = "STORE";

        InventoryEntity entity = new InventoryEntity.Builder(id)
                .sku(sku)
                .location(location)
                .quantity(quantity)
                .type(type)
                .build();

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getSku()).isEqualTo(sku);
        assertThat(entity.getLocation()).isEqualTo(location);
        assertThat(entity.getQuantity()).isEqualTo(quantity);
        assertThat(entity.getType()).isEqualTo(type);
    }

    @Test
    void setters_WhenValuesAreChanged_ThenExposeUpdatedValues() {
        InventoryEntity entity = new InventoryEntity.Builder(UUID.randomUUID()).build();

        entity.setSku("SKU456");
        entity.setLocation(100);
        entity.setQuantity(25);
        entity.setType("WAREHOUSE");

        assertThat(entity.getSku()).isEqualTo("SKU456");
        assertThat(entity.getLocation()).isEqualTo(100);
        assertThat(entity.getQuantity()).isEqualTo(25);
        assertThat(entity.getType()).isEqualTo("WAREHOUSE");
    }

    @Test
    void builder_WhenIdIsNull_ThenThrowsNullPointerException() {
        assertThatNullPointerException().isThrownBy(() -> new InventoryEntity.Builder(null));
    }
}
