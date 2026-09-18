package dev.swang.ecommerce.inventoryservice;

public final class InventoryMapper {

    public static Inventory toDomain(InventoryEntity entity) {
        return new Inventory(entity.getId(), entity.getSkuCode(), entity.getQuantity());
    }



}
