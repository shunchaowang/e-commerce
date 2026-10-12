package dev.swang.ecommerce.utils;

import org.springframework.stereotype.Component;

import dev.swang.ecommerce.inventoryservice.api.InventoryResponse;
import dev.swang.ecommerce.inventoryservice.model.Inventory;
import dev.swang.ecommerce.inventoryservice.persistence.InventoryEntity;

@Component
public class InventoryMapper {

  public Inventory entityToInventory(InventoryEntity entity) {
    return new Inventory.Builder().id(entity.getId()).sku(entity.getSku())
        .location(entity.getLocation()).quantity(entity.getQuantity()).build();
  }

  public InventoryEntity inventoryToEntity(Inventory inventory) {
    return new InventoryEntity.Builder(inventory.getId()).sku(inventory.getSku())
        .location(inventory.getLocation()).quantity(inventory.getQuantity())
        .type(inventory.getType().name()).build();
  }

  public InventoryResponse inventoryToInventoryResponse(Inventory inventory) {
    return new InventoryResponse(inventory.getSku(), inventory.getLocation(), inventory.getType().name(),
        inventory.getQuantity());
  }

}
