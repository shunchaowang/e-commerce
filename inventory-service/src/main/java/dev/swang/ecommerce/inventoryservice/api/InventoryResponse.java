package dev.swang.ecommerce.inventoryservice.api;

public record InventoryResponse(String sku, Integer location, String type, Integer quantity) {

}
