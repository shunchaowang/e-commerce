package dev.swang.ecommerce.inventoryservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import dev.swang.ecommerce.inventoryservice.model.Inventory;
import dev.swang.ecommerce.inventoryservice.model.InventoryType;
import dev.swang.ecommerce.inventoryservice.persistence.InventoryEntity;
import dev.swang.ecommerce.inventoryservice.persistence.InventoryRepository;
import dev.swang.ecommerce.utils.InventoryMapper;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

  @Mock
  private InventoryRepository inventoryRepository;

  private InventoryMapper inventoryMapper;
  private InventoryService inventoryService;

  @BeforeEach
  void setUp() {
    inventoryMapper = new InventoryMapper();
    inventoryService = new InventoryService(inventoryRepository, inventoryMapper);
  }

  @Test
  void ifExists_WhenInventoryExists_ThenReturnTrue() {
    String sku = "SKU123";
    Integer location = 899;
    UUID id = UUID.randomUUID();
    InventoryEntity inventoryEntity = new InventoryEntity.Builder(id).sku(sku).location(location)
        .type(InventoryType.STORE.name()).quantity(10).build();
    when(inventoryRepository.findBySkuAndLocation(sku, location))
        .thenReturn(Optional.of(inventoryEntity));

    boolean result = inventoryService.ifExists(sku, location);

    assertThat(result).isTrue();
  }

  @Test
  void ifExists_WhenInventoryNotFound_ThenReturnFalse() {
    String sku = "SKU123";
    Integer location = 899;
    when(inventoryRepository.findBySkuAndLocation(sku, location)).thenReturn(Optional.empty());

    boolean result = inventoryService.ifExists(sku, location);

    assertThat(result).isFalse();
  }

  @Test
  void get_WhenInventoryExists_ThenReturnInventory() {
    String sku = "SKU123";
    Integer location = 899;
    UUID id = UUID.randomUUID();
    InventoryEntity inventoryEntity = new InventoryEntity.Builder(id).sku(sku).location(location)
        .type(InventoryType.STORE.name()).quantity(10).build();
    when(inventoryRepository.findBySkuAndLocation(sku, location))
        .thenReturn(Optional.of(inventoryEntity));

    Optional<Inventory> result = inventoryService.get(sku, location);

    assertThat(result).isPresent();
    assertThat(result.get().getSku()).isEqualTo(sku);
    assertThat(result.get().getLocation()).isEqualTo(location);
  }

  @Test
  void get_WhenInventoryNotExists_ThenReturnEmpty() {
    String sku = "SKU123";
    Integer location = 899;
    when(inventoryRepository.findBySkuAndLocation(sku, location)).thenReturn(Optional.empty());

    Optional<Inventory> result = inventoryService.get(sku, location);

    assertThat(result).isNotPresent();
  }

  @Test
  void getAll_WhenInventoryExists_GivenNoPagination_ThenReturnInventoryList() {
    String sku = "SKU123";
    Integer location = 899;
    UUID id = UUID.randomUUID();
    InventoryEntity inventoryEntity = new InventoryEntity.Builder(id).sku(sku).location(location)
        .type(InventoryType.STORE.name()).quantity(10).build();
    when(inventoryRepository.findAll()).thenReturn(java.util.List.of(inventoryEntity));

    java.util.List<Inventory> result = inventoryService.getAll(-1, -1);

    assertThat(result).isNotEmpty();
    assertThat(result.get(0).getSku()).isEqualTo(sku);
    assertThat(result.get(0).getLocation()).isEqualTo(location);
  }

  @Test
  void getAll_WhenInventoryExists_GivenPagination_ThenReturnInventoryList() {
    List<InventoryEntity> inventoryEntities = new ArrayList<>();
    for (int i = 0; i < 11; i++) {
      String sku = "SKU" + i;
      Integer location = 899 + i;
      UUID id = UUID.randomUUID();
      InventoryEntity inventoryEntity = new InventoryEntity.Builder(id).sku(sku).location(location)
          .type(InventoryType.STORE.name()).quantity(10).build();
      inventoryEntities.add(inventoryEntity);
    }
    when(inventoryRepository.findAll(PageRequest.of(0, 10)))
        .thenReturn(new PageImpl<>(inventoryEntities));

    List<Inventory> result = inventoryService.getAll(0, 10);

    assertThat(result).hasSize(10);
  }



  @Test
  void create_WhenInventoryNotExists_ThenReturnInventory() {
    String sku = "SKU123";
    Integer location = 899;
    int quantity = 10;
    InventoryType type = InventoryType.STORE;
    InventoryEntity inventoryEntity = new InventoryEntity.Builder(UUID.randomUUID()).sku(sku)
        .location(location).type(type.name()).quantity(quantity).build();
    when(inventoryRepository.findBySkuAndLocation(sku, location)).thenReturn(Optional.empty());
    when(inventoryRepository.save(inventoryEntity)).thenReturn(inventoryEntity);

    Inventory result = inventoryService.create(inventoryMapper.entityToInventory(inventoryEntity));

    assertThat(result).isNotNull();
    assertThat(result.getSku()).isEqualTo(sku);
    assertThat(result.getLocation()).isEqualTo(location);
  }

  @Test
  void create_WhenInventoryExists_ThenThrowException() {
    String sku = "SKU123";
    Integer location = 899;
    int quantity = 10;
    InventoryType type = InventoryType.STORE;
    InventoryEntity inventoryEntity = new InventoryEntity.Builder(UUID.randomUUID()).sku(sku)
        .location(location).type(type.name()).quantity(quantity).build();
    when(inventoryRepository.findBySkuAndLocation(sku, location))
        .thenReturn(Optional.of(inventoryEntity));

    Inventory inventory = inventoryMapper.entityToInventory(inventoryEntity);
    assertThrows(RuntimeException.class, () -> inventoryService.create(inventory),
        "Inventory already exists for sku: " + sku + " and location: " + location);
  }

  @Test
  void update_WhenInventoryNotExists_ThenThrowException() {
    String sku = "SKU123";
    Integer location = 899;
    int quantity = 10;
    when(inventoryRepository.findBySkuAndLocation(sku, location)).thenReturn(Optional.empty());

    Inventory inventory = new Inventory.Builder().sku(sku).location(location).quantity(quantity).build();
    assertThrows(RuntimeException.class, () -> inventoryService.update(inventory),
        "Inventory does not exist for sku: " + sku + " and location: " + location);
  }

  @Test
  void update_WhenInventoryExists_ThenReturnInventory() {
    String sku = "SKU123";
    Integer location = 899;
    int quantity = 10;
    InventoryEntity inventoryEntity = new InventoryEntity.Builder(UUID.randomUUID()).sku(sku)
        .location(location).type(InventoryType.STORE.name()).quantity(10).build();
    when(inventoryRepository.findBySkuAndLocation(sku, location))
        .thenReturn(Optional.of(inventoryEntity));

    Inventory result = inventoryService.update(inventoryMapper.entityToInventory(inventoryEntity));

    assertThat(result).isNotNull();
    assertThat(result.getQuantity()).isEqualTo(quantity);
  }

  @Test
  void delete_WhenInventoryExists_ThenReturnInventory() {
    String sku = "SKU123";
    Integer location = 899;
    InventoryEntity inventoryEntity = new InventoryEntity.Builder(UUID.randomUUID()).sku(sku)
        .location(location).type(InventoryType.STORE.name()).quantity(10).build();
    when(inventoryRepository.findBySkuAndLocation(sku, location))
        .thenReturn(Optional.of(inventoryEntity));

    Inventory result = inventoryService.delete(sku, location);

    assertThat(result).isNotNull();
    assertThat(result.getSku()).isEqualTo(sku);
    assertThat(result.getLocation()).isEqualTo(location);
  }

  @Test
  void delete_WhenInventoryNotExists_ThenThrowException() {
    String sku = "SKU123";
    Integer location = 899;
    when(inventoryRepository.findBySkuAndLocation(sku, location)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> inventoryService.delete(sku, location),
        "Inventory does not exist for sku: " + sku + " and location: " + location);
  }

}
