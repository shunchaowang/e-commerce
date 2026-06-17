package dev.swang.ecommerce.inventoryservice.service;

import dev.swang.ecommerce.inventoryservice.model.Inventory;
import dev.swang.ecommerce.inventoryservice.repository.InventoryRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

  private final InventoryRepository inventoryRepository;

  public InventoryService(InventoryRepository inventoryRepository) {
    this.inventoryRepository = inventoryRepository;
  }

  @Transactional(readOnly = true)
  public boolean InStock(String skuCode, Integer quantity) {
    return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
  }

  @Transactional(readOnly = true)
  public Optional<Inventory> getInventoryBySkuCode(String skuCode) {
    return inventoryRepository.findBySkuCode(skuCode);
  }

  @Transactional
  public Inventory enStock(String skuCode, Integer quantity) {

    Inventory inventory = getInventoryBySkuCode(skuCode).orElse(new Inventory());
    inventory.setQuantity(inventory.getQuantity() + quantity);
    return inventoryRepository.save(inventory);
  }

  @Transactional
  public Inventory outStock(String skuCode, Integer quantity) {
    Inventory inventory = getInventoryBySkuCode(skuCode).orElseThrow(
        () -> new RuntimeException("Inventory not found"));
    if (inventory.getQuantity() < quantity) {
      throw new RuntimeException("Inventory not enough");
    }
    inventory.setQuantity(inventory.getQuantity() - quantity);
    return inventoryRepository.save(inventory);
  }
}
