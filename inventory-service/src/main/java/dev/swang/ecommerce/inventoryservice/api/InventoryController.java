package dev.swang.ecommerce.inventoryservice.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import dev.swang.ecommerce.inventoryservice.model.Inventory;
import dev.swang.ecommerce.inventoryservice.service.InventoryService;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

  // visibility-scope-mutability
  private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);
  private final InventoryService inventoryService;

  public InventoryController(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @GetMapping("/{skuCode}")
  @ResponseStatus (HttpStatus.OK)
  public Integer getStock(@PathVariable String skuCode) {
    return inventoryService.getStock(skuCode);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CreateInventoryResponse enStock(@RequestBody CreateInventoryRequest request) {
    Inventory inventory = inventoryService.enStock(request.skuCode(), request.quantity());
    return new CreateInventoryResponse(inventory.getSkuCode(), inventory.getQuantity());
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public CreateInventoryResponse outStock(@RequestBody CreateInventoryRequest request) {
    Inventory inventory = inventoryService.outStock(request.skuCode(), request.quantity());
    return new CreateInventoryResponse(inventory.getSkuCode(), inventory.getQuantity());
  }

}
