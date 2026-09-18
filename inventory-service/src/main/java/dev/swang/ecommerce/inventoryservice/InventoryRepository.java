package dev.swang.ecommerce.inventoryservice;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Integer> {

  boolean existsBySkuCodeAndQuantityIsGreaterThanEqual(String skuCode, Integer quantity);

  Optional<InventoryEntity> findBySkuCode(String skuCode);
}
