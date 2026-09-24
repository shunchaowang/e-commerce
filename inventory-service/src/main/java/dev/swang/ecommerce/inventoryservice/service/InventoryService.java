package dev.swang.ecommerce.inventoryservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.swang.ecommerce.inventoryservice.model.Inventory;
import dev.swang.ecommerce.inventoryservice.persistence.InventoryEntity;
import dev.swang.ecommerce.inventoryservice.persistence.InventoryRepository;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private static final String inventoryNotFoundStr = "Inventory not found for skuCode: ";

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public boolean ifExists(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }

    // we will check if the skuCode exists in the database, if it does, we will update the quantity
    // by adding the passed in quantity,
    // if it does not exist, we will create a new inventory with the passed in skuCode and quantity
    @Transactional
    public Inventory enStock(String skuCode, int quantity) {

        InventoryEntity inventoryEntity = inventoryRepository.findBySkuCode(skuCode)
                .orElseGet(() -> new InventoryEntity(skuCode, 0));
        // now inventoryEntity either an existing inventory or a new one with quantity 0
        // conver it to the Inventory
        Inventory inventory = inventoryEntity.toInventory();
        inventory.changeQuantity(inventory.getQuantity() + quantity);
        inventoryEntity.setQuantity(inventory.getQuantity());
        return inventory;
    }

    @Transactional(readOnly = true)
    public Inventory getInventoryBySkuCode(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode).map(InventoryEntity::toInventory)
                .orElseThrow(() -> {
                    return new IllegalArgumentException(inventoryNotFoundStr + skuCode);
                });
    }


    @Transactional
    public Inventory outStock(String skuCode, int quantity) {
        InventoryEntity inventoryEntity = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new IllegalArgumentException(inventoryNotFoundStr + skuCode));
        Inventory inventory = inventoryEntity.toInventory();
        if (inventory.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough inventory for skuCode: " + skuCode);
        }
        inventory.changeQuantity(inventory.getQuantity() - quantity);
        inventoryEntity.setQuantity(inventory.getQuantity());
        return inventory;
    }

    @Transactional(readOnly = true)
    public Integer getStock(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode).map(InventoryEntity::getQuantity)
                .orElseThrow(() -> new IllegalArgumentException(inventoryNotFoundStr + skuCode));
    }
}
