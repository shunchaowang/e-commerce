package dev.swang.ecommerce.inventoryservice.api;

public record QueryInventoryRequest(String skuCode, Integer quantity) {

}
