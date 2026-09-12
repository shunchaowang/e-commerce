package dev.swang.ecommerce.inventoryservice;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

  boolean existsBySkuCodeAndQuantityIsGreaterThanEqual(String skuCode, Integer quantity);

  Optional<Inventory> findBySkuCode(String skuCode);
}
