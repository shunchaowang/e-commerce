package dev.swang.ecommerce.inventoryservice.api;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.swang.ecommerce.inventoryservice.config.BadRequestException;
import dev.swang.ecommerce.inventoryservice.model.Inventory;
import dev.swang.ecommerce.inventoryservice.service.InventoryService;
import dev.swang.ecommerce.utils.InventoryMapper;

@RestController
@RequestMapping("/api/v1/inventory/{skuCode}")
public class InventoryController {

  // visibility-scope-mutability
  private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);
  private final InventoryService inventoryService;
  private final InventoryMapper inventoryMapper;

  public InventoryController(InventoryService inventoryService, InventoryMapper inventoryMapper) {
    this.inventoryService = inventoryService;
    this.inventoryMapper = inventoryMapper;
  }

  @GetMapping
  public ResponseEntity<InventoryResponse> getStock(@PathVariable String skuCode) {
    try {
      Integer quantity = inventoryService.getStock(skuCode);
      return ResponseEntity.ok(new InventoryResponse(skuCode, quantity));
    } catch (BadRequestException e) {
      logger.error("Error getting stock for skuCode: {}", skuCode, e);
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Adds stock to the inventory for the given SKU code. This is to show how to
   * path variable,
   * request param and request body in the same method. path variable is used to
   * identify the
   * resource, request param is used to pass additional information like filter,
   * and request body is
   * used to pass the data. post /api/v1/inventory/{skuCode}?valid=true
   * {"quantity": 10 }. This api
   * is used to add stock to the inventory for the given SKU code.
   * 
   * @param skuCode which sku to want to add ventory
   * @param valid   should validate if the sku exists or not. if valid is true, we
   *                will check if the
   *                sku exists in the database, if it does not exist, we will
   *                throw an exception; if valid
   *                is false, we will not check if the sku exists in the database,
   *                we will just add the
   *                stock to the inventory if the sku does not exist, we will
   *                create a new inventory with
   *                the given sku and quantity.
   * @param request the request body containing the quantity to add to the
   *                inventory, to make this
   *                generic, we can use the same request body for both enStock and
   *                outStock methods.
   * @return
   */
  @PostMapping
  public ResponseEntity<InventoryResponse> enStock(@PathVariable String skuCode,
      @RequestParam boolean valid, @RequestBody CommandInventoryRequest request) {
    if (valid) {
      // check if the sku exists in the database, if it does not exist, it's a bad
      // request, we will
      // throw an exception
      if (!inventoryService.ifExists(skuCode)) {
        return ResponseEntity.notFound().build();
      } else {
        Inventory inventory = inventoryService.enStock(skuCode, request.quantity());
        return ResponseEntity
            .created(URI.create("/api/v1/inventory/" + inventory.getSkuCode()))
            .body(inventoryMapper.inventoryToInventoryResponse(inventory));
      }
    } else {
      Inventory inventory = inventoryService.enStock(skuCode, request.quantity());
      return ResponseEntity
          .created(URI.create("/api/v1/inventory/" + skuCode))
          .body(inventoryMapper.inventoryToInventoryResponse(inventory));
    }
  }

  @PutMapping
  public ResponseEntity<InventoryResponse> outStock(@PathVariable String skuCode,
      @RequestBody CommandInventoryRequest request) {
    // Assuming there's a method in inventoryService to handle outStock with just
    // quantity
    Inventory inventory = inventoryService.outStock(skuCode, request.quantity());
    return ResponseEntity
        .ok(inventoryMapper.inventoryToInventoryResponse(inventory));
  }

}
