package dev.swang.ecommerce.inventoryservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import dev.swang.ecommerce.inventoryservice.model.Inventory;
import dev.swang.ecommerce.inventoryservice.persistence.InventoryEntity;
import dev.swang.ecommerce.inventoryservice.persistence.InventoryRepository;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

  @Mock
  private InventoryRepository inventoryRepository;

  @InjectMocks
  private InventoryService inventoryService;


  @Test
  void ifExists_WhenInventoryExists_ThenReturnTrue() {
    String skuCode = "SKU123";
    InventoryEntity inventoryEntity = new InventoryEntity(skuCode, 10);
    when(inventoryRepository.findBySkuCode(skuCode)).thenReturn(Optional.of(inventoryEntity));

    boolean result = inventoryService.ifExists(skuCode);

    assertThat(result).isTrue();
  }

  @Test
  void ifExists_WhenInventoryNotFound_ThenReturnFalse() {
    String skuCode = "SKU123";
    when(inventoryRepository.findBySkuCode(skuCode)).thenReturn(Optional.empty());

    boolean result = inventoryService.ifExists(skuCode);

    assertThat(result).isFalse();
  }

  @Test
  void enStock_WhenInventoryExists_ThenReturnInventory() {
    String skuCode = "SKU123";
    int quantity = 10;
    InventoryEntity inventoryEntity = new InventoryEntity(skuCode, quantity);
    when(inventoryRepository.findBySkuCode(skuCode)).thenReturn(Optional.of(inventoryEntity));

    Inventory result = inventoryService.enStock(skuCode, quantity);

    assertThat(result).isNotNull();
    assertThat(result.getQuantity()).isEqualTo(quantity + quantity);
  }

  @Test
  void enStock_WhenInventoryNotExists_ThenReturnInventory() {
    String skuCode = "SKU123";
    int quantity = 10;
    when(inventoryRepository.findBySkuCode(skuCode)).thenReturn(Optional.empty());

    Inventory result = inventoryService.enStock(skuCode, quantity);

    assertThat(result).isNotNull();
    assertThat(result.getQuantity()).isEqualTo(quantity);
  }

  @Test
  void outStock_WhenInventoryExists_ThenReturnInventory() {
    String skuCode = "SKU123";
    int quantity = 10;
    int outStockQuantity = 6;
    InventoryEntity inventoryEntity = new InventoryEntity(skuCode, quantity);

    when(inventoryRepository.findBySkuCode(skuCode)).thenReturn(Optional.of(inventoryEntity));

    Inventory result = inventoryService.outStock(skuCode, outStockQuantity);

    assertThat(result).isNotNull();
    assertThat(result.getQuantity()).isEqualTo(quantity - outStockQuantity);
  }

  @Test
  void outStock_WhenInventoryNotFound_ThenThrowException() {
    String skuCode = "SKU123";
    int outStockQuantity = 6;
    when(inventoryRepository.findBySkuCode(skuCode)).thenReturn(Optional.empty());
    assertThrows(RuntimeException.class, () -> inventoryService.outStock(skuCode, outStockQuantity),
        "Inventory not found for skuCode: " + skuCode);
  }

  @Test
  void outStock_WhenInventoryNotEnough_ThenThrowException() {
    String skuCode = "SKU123";
    int quantity = 4;
    int outStockQuantity = 6;
    InventoryEntity inventoryEntity = new InventoryEntity(skuCode, quantity);
    when(inventoryRepository.findBySkuCode(skuCode)).thenReturn(Optional.of(inventoryEntity));
    assertThrows(RuntimeException.class, () -> inventoryService.outStock(skuCode, outStockQuantity),
        "Not enough inventory for skuCode: " + skuCode);
  }

}
