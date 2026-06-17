package dev.swang.ecommerce.inventoryservice.repository;

import dev.swang.ecommerce.inventoryservice.model.Inventory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

  boolean existsBySkuCodeAndQuantityIsGreaterThanEqual(String skuCode, Integer quantity);

  Optional<Inventory> findBySkuCode(String skuCode);
}
