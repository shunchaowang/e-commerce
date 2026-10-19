package dev.swang.ecommerce.utils;

import java.util.UUID;
import org.springframework.stereotype.Component;
import dev.swang.ecommerce.inventoryservice.api.CreateInventoryRequest;
import dev.swang.ecommerce.inventoryservice.api.EnOutStockRequest;
import dev.swang.ecommerce.inventoryservice.api.InventoryResponse;
import dev.swang.ecommerce.inventoryservice.api.UpdateInventoryTypeRequest;
import dev.swang.ecommerce.inventoryservice.model.Inventory;
import dev.swang.ecommerce.inventoryservice.model.InventoryType;
import dev.swang.ecommerce.inventoryservice.persistence.InventoryEntity;

@Component
public class InventoryMapper {

  public Inventory entityToInventory(InventoryEntity entity) {
    return new Inventory.Builder().id(entity.getId()).sku(entity.getSku())
        .location(entity.getLocation()).type(InventoryType.valueOf(entity.getType()))
        .quantity(entity.getQuantity()).build();
  }

  public InventoryEntity inventoryToEntity(Inventory inventory) {
    return new InventoryEntity.Builder(inventory.getId()).sku(inventory.getSku())
        .location(inventory.getLocation()).quantity(inventory.getQuantity())
        .type(inventory.getType().name()).build();
  }

  public InventoryResponse inventoryToInventoryResponse(Inventory inventory) {
    return new InventoryResponse(inventory.getSku(), inventory.getLocation(),
        inventory.getType().name(), inventory.getQuantity());
  }

  public Inventory createInventoryRequestToInventory(CreateInventoryRequest request) {
    return new Inventory.Builder().id(UUID.randomUUID()).sku(request.sku())
        .location(request.location()).quantity(request.quantity())
        .type(InventoryType.valueOf(request.type())).build();
  }

  public Inventory enOutStockRequestToInventory(EnOutStockRequest request) {
    return new Inventory.Builder().sku(request.sku()).location(request.location())
        .quantity(request.quantity()).build();
  }

  public Inventory updateInventoryTypeRequestToInventory(UpdateInventoryTypeRequest request) {
    return new Inventory.Builder().sku(request.sku()).location(request.location())
        .type(InventoryType.valueOf(request.type())).build();
  }

}
