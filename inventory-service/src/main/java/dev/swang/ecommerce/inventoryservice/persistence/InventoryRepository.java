package dev.swang.ecommerce.inventoryservice.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    Optional<InventoryEntity> findBySkuCode(String skuCode);
}
