package dev.swang.ecommerce.inventoryservice.api;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dev.swang.ecommerce.inventoryservice.model.Inventory;
import dev.swang.ecommerce.inventoryservice.service.InventoryService;
import dev.swang.ecommerce.utils.InventoryMapper;
import org.springframework.web.bind.annotation.PutMapping;


/**
 * 
 * InventoryController In this REST controller, we should have the method as the following. The
 * first one, we should be able to get the location inventory of the SKU. That means we pass in a
 * SKU and a specific location. We should be able to get the inventory for that location with SKU.
 * The second one, we should be able to get a pageable inventory for a specific SKU. The third one,
 * we should be able to get the inventory for the specific location. The fourth one, we should be
 * able to get all the inventory. Definitely it needs to be pageable. The fifth one, we should be
 * able to create a new SKU inventory for the location. The sixth one, we should be able to in-stock
 * some quantity for the specific location with a specific SKU. The seventh one, we need to be able
 * to out-stock a SKU from an inventory on the location. Do we need to delete? Yes, we should be
 * able to delete a SKU and the location combination.
 */
@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

  // visibility-scope-mutability
  private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);
  private final InventoryService inventoryService;
  private final InventoryMapper inventoryMapper;

  public InventoryController(InventoryService inventoryService, InventoryMapper inventoryMapper) {
    this.inventoryService = inventoryService;
    this.inventoryMapper = inventoryMapper;
  }

  @GetMapping("/{sku}/{location}")
  public ResponseEntity<InventoryResponse> getInventoryBySkuAndLocation(@PathVariable String sku,
      @PathVariable Integer location) {
    Optional<Inventory> inventory = inventoryService.get(sku, location);
    if (inventory.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    InventoryResponse response = inventoryMapper.inventoryToInventoryResponse(inventory.get());
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<InventoryResponse>> getAllInventory(@RequestParam int offset,
      @RequestParam int limit) {
    List<InventoryResponse> response = inventoryService.getAll(offset, limit).stream()
        .map(inventoryMapper::inventoryToInventoryResponse).toList();
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<InventoryResponse> createInventory(
      @RequestBody CreateInventoryRequest request) {

    Inventory inventory = inventoryMapper.createInventoryRequestToInventory(request);
    inventory = inventoryService.create(inventory);
    logger.info("Created inventory for sku: {} and location: {}", request.sku(),
        request.location());
    return ResponseEntity.ok(inventoryMapper.inventoryToInventoryResponse(inventory));
  }

  @PutMapping
  public ResponseEntity<InventoryResponse> updateInventoryType(
      @RequestBody UpdateInventoryTypeRequest request) {
    Inventory inventory = inventoryMapper.updateInventoryTypeRequestToInventory(request);
    inventory = inventoryService.update(inventory);
    logger.info("Updated inventory type for sku: {} and location: {}", request.sku(),
        request.location());
    return ResponseEntity.ok(inventoryMapper.inventoryToInventoryResponse(inventory));
  }

  @PatchMapping
  public ResponseEntity<InventoryResponse> enStock(@RequestBody EnOutStockRequest request) {
    Inventory inventory = inventoryMapper.enOutStockRequestToInventory(request);
    inventory.changeQuantity(inventory.getQuantity() + request.quantity());
    inventory = inventoryService.update(inventory);
    logger.info("In-stocked {} units of SKU {} at location {}", request.quantity(), request.sku(),
        request.location());
    return ResponseEntity.ok(inventoryMapper.inventoryToInventoryResponse(inventory));
  }

  @PatchMapping
  public ResponseEntity<InventoryResponse> outStock(@RequestBody EnOutStockRequest request) {
    Inventory inventory = inventoryMapper.enOutStockRequestToInventory(request);
    inventory.changeQuantity(inventory.getQuantity() - request.quantity());
    inventory = inventoryService.update(inventory);
    logger.info("Out-stocked {} units of SKU {} from location {}", request.quantity(),
        request.sku(), request.location());
    return ResponseEntity.ok(inventoryMapper.inventoryToInventoryResponse(inventory));
  }

  @DeleteMapping("/{sku}/{location}")
  public ResponseEntity<InventoryResponse> deleteInventory(@PathVariable String sku,
      @PathVariable Integer location) {
    inventoryService.delete(sku, location);
    logger.info("Deleted inventory for sku: {} and location: {}", sku, location);
    return ResponseEntity.noContent().build();
  }

}
