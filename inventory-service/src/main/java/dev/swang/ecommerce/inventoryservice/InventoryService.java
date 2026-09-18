package dev.swang.ecommerce.inventoryservice;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.swang.ecommerce.inventoryservice.config.BadRequestException;

@Service
public class InventoryService {

  private final InventoryRepository inventoryRepository;

  public InventoryService(InventoryRepository inventoryRepository) {
    this.inventoryRepository = inventoryRepository;
  }

  @Transactional(readOnly = true)
  public boolean inStock(String skuCode, Integer quantity) {
    return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
  }

  @Transactional(readOnly = true)
  public Optional<InventoryEntity> getInventoryBySkuCode(String skuCode) {
    return inventoryRepository.findBySkuCode(skuCode);
  }

  @Transactional
  public InventoryEntity enStock(String skuCode, Integer quantity) {

    InventoryEntity inventory =
        inventoryRepository.findBySkuCode(skuCode).orElse(new InventoryEntity.Builder().build());
    inventory.setQuantity(inventory.getQuantity() + quantity);
    return inventoryRepository.save(inventory);
  }

  @Transactional
  public InventoryEntity outStock(String skuCode, Integer quantity) {
    InventoryEntity inventory = inventoryRepository.findBySkuCode(skuCode)
        .orElseThrow(() -> new BadRequestException("Inventory not found"));
    if (inventory.getQuantity() < quantity) {
      throw new BadRequestException("Inventory not enough");
    }
    inventory.setQuantity(inventory.getQuantity() - quantity);
    return inventoryRepository.save(inventory);
  }
}
