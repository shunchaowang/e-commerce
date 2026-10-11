package dev.swang.ecommerce.inventoryservice.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID> {
    Optional<InventoryEntity> findBySkuAndLocation(String sku, Integer location);

    Page<InventoryEntity> findBySku(String sku, Pageable pageable);

    Page<InventoryEntity> findByLocation(Integer location, Pageable pageable);

}
