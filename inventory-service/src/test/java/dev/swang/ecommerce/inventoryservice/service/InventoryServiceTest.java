package dev.swang.ecommerce.inventoryservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.swang.ecommerce.inventoryservice.model.Inventory;
import dev.swang.ecommerce.inventoryservice.repository.InventoryRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

  @Mock
  private InventoryRepository inventoryRepository;

  @InjectMocks
  private InventoryService inventoryService;

  @Test
  void outStock_WhenInventoryExists_ThenReturnInventory() {
    Inventory inventory = new Inventory.Builder()
        .skuCode("SKU123")
        .quantity(10)
        .build();

    when(inventoryRepository.findBySkuCode("SKU123")).thenReturn(Optional.of(inventory));

    Inventory result = inventoryService.outStock("SKU123", 6);

    assertThat(result).isNotNull();
    assertThat(result.getQuantity()).isEqualTo(4);
    verify(inventoryRepository, times(1)).save(inventory);

  }

  void outStock_WhenInventoryNotFound_ThenThrowException() {

  }

  void outStock_WhenInventoryNotEnough_ThenThrowException() {

  }

}
