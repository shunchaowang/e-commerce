package dev.swang.ecommerce.inventoryservice.controller;

import dev.swang.ecommerce.inventoryservice.model.Inventory;
import dev.swang.ecommerce.inventoryservice.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

  // visibility-scope-mutability
  private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);
  private final InventoryService inventoryService;

  public InventoryController(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @GetMapping
  public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity) {
    return inventoryService.InStock(skuCode, quantity);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public InventoryDTO enStock(@RequestBody InventoryDTO inventoryDTO) {
    Inventory inventory = inventoryService.enStock(inventoryDTO.skuCode(), inventoryDTO.quantity());
    return new InventoryDTO(inventory.getSkuCode(), inventory.getQuantity());
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public InventoryDTO outStock(@RequestBody InventoryDTO inventoryDTO) {
    Inventory inventory = inventoryService.outStock(inventoryDTO.skuCode(),
        inventoryDTO.quantity());
    return new InventoryDTO(inventory.getSkuCode(), inventory.getQuantity());
  }

  public record InventoryDTO(String skuCode, Integer quantity) {

  }

}
