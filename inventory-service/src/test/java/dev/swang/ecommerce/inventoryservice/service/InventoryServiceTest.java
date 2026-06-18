package dev.swang.ecommerce.inventoryservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    String skuCode = "SKU123";
    int quantity = 10;
    int outStockQuantity = 6;
    Inventory inventory = new Inventory.Builder()
        .skuCode(skuCode)
        .quantity(quantity)
        .build();

    when(inventoryRepository.findBySkuCode(skuCode)).thenReturn(Optional.of(inventory));
    // remember to mock the save method
    when(inventoryRepository.save(inventory)).thenReturn(inventory);

    Inventory result = inventoryService.outStock(skuCode, outStockQuantity);

    assertThat(result).isNotNull();
    assertThat(result.getQuantity()).isEqualTo(quantity - outStockQuantity);
    verify(inventoryRepository, times(1)).save(inventory);

  }

  @Test
  void outStock_WhenInventoryNotFound_ThenThrowException() {
    String skuCode = "SKU123";
    int outStockQuantity = 6;
    when(inventoryRepository.findBySkuCode(skuCode)).thenReturn(Optional.empty());
    assertThrows(RuntimeException.class,
        () -> inventoryService.outStock(skuCode, outStockQuantity), "Inventory not found");
  }

  @Test
  void outStock_WhenInventoryNotEnough_ThenThrowException() {
    String skuCode = "SKU123";
    int quantity = 4;
    int outStockQuantity = 6;
    Inventory inventory = new Inventory.Builder()
        .skuCode(skuCode)
        .quantity(quantity)
        .build();
    when(inventoryRepository.findBySkuCode(skuCode)).thenReturn(Optional.of(inventory));
    assertThrows(RuntimeException.class,
        () -> inventoryService.outStock(skuCode, outStockQuantity), "Inventory not enough");
  }

}
