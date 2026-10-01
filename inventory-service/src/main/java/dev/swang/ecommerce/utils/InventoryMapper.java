package dev.swang.ecommerce.utils;

import org.springframework.stereotype.Component;

import dev.swang.ecommerce.inventoryservice.api.InventoryResponse;
import dev.swang.ecommerce.inventoryservice.model.Inventory;
import dev.swang.ecommerce.inventoryservice.persistence.InventoryEntity;

@Component
public class InventoryMapper {

  public Inventory entityToInventory(InventoryEntity entity) {
    return new Inventory.Builder().id(entity.getId()).skuCode(entity.getSkuCode()).quantity(entity.getQuantity())
        .build();
  }

  public InventoryEntity inventoryToEntity(Inventory inventory) {
    InventoryEntity entity = new InventoryEntity(inventory.getSkuCode(), inventory.getQuantity());
    entity.setId(inventory.getId() != null ? inventory.getId() : null);
    return entity;
  }

  public InventoryResponse inventoryToInventoryResponse(Inventory inventory) {
    return new InventoryResponse(inventory.getSkuCode(), inventory.getQuantity());
  }

}
