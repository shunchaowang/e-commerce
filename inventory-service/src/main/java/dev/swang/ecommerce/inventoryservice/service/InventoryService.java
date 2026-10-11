package dev.swang.ecommerce.inventoryservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException.NotImplemented;

import dev.swang.ecommerce.inventoryservice.config.BadRequestException;
import dev.swang.ecommerce.inventoryservice.model.Inventory;
import dev.swang.ecommerce.inventoryservice.model.InventoryId;
import dev.swang.ecommerce.inventoryservice.model.InventoryType;
import dev.swang.ecommerce.inventoryservice.persistence.InventoryEntity;
import dev.swang.ecommerce.inventoryservice.persistence.InventoryRepository;
import dev.swang.ecommerce.utils.InventoryMapper;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private static final String inventoryNotFoundStr = "Inventory not found for sku: ";

    public InventoryService(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @Transactional
    public Boolean ifExists(String sku, Integer location) {
        return false;
    }

    @Transactional
    public Optional<Inventory> get(String sku, Integer location) {
        return Optional.empty();
    }

    @Transactional
    public Optional<List<Inventory>> get(String sku, Integer location, int offset, int limit) {
        return Optional.empty();
    }

    @Transactional
    public Inventory enable(String sku, Integer location) {
        return null;
    }

    @Transactional
    public Inventory disable(String sku, Integer location) {
        return null;
    }

    /**
     * Create a new inventory of the type, if the sku/location already exists, throw
     * the exception.
     * 
     * @param sku
     * @param location
     * @param quantity
     * @param type
     * @return
     */
    @Transactional(readOnly = false)
    public Inventory create(String sku, Integer location, Integer quantity, InventoryType type) {
        return null;
    }

    /**
     * Add the inventory of quantity to the existing one, throw exception if it
     * doesn't exist.
     * 
     * @param sku
     * @param location
     * @param quantity
     * @return
     */
    @Transactional(readOnly = false)
    public Inventory enStock(String sku, Integer location, Integer quantity) {
        return null;
    }

    /**
     * Remove the inventory for location of sku, throw exception if it doesn't exist
     * or there
     * is not enough
     * 
     * @param sku
     * @param location
     * @param quantity
     * @return
     */
    @Transactional(readOnly = false)
    public Inventory outStock(String sku, Integer location, Integer quantity) {
        return null;
    }

    @Transactional(readOnly = false)
    public Inventory delete(String sku, Integer location) {
        return null;
    }
    // todo: delete from here

    // we will check if the skuCode exists in the database, if it does, we will
    // update the quantity
    // by adding the passed in quantity,
    // if it does not exist, we will create a new inventory with the passed in
    // skuCode and quantity
    @Transactional
    public Inventory enStock(String skuCode, int quantity) {

        InventoryEntity inventoryEntity = inventoryRepository.findBySkuCode(skuCode)
                .orElseGet(() -> new InventoryEntity(skuCode, 0));
        // now inventoryEntity either an existing inventory or a new one with quantity 0
        // conver it to the Inventory
        Inventory inventory = inventoryMapper.entityToInventory(inventoryEntity);
        inventory.changeQuantity(inventory.getQuantity() + quantity);
        inventoryEntity.setQuantity(inventory.getQuantity());
        return inventory;
    }

    @Transactional
    public Inventory outStock(String skuCode, int quantity) {
        InventoryEntity inventoryEntity = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new BadRequestException(inventoryNotFoundStr + skuCode));
        Inventory inventory = inventoryMapper.entityToInventory(inventoryEntity);
        if (inventory.getQuantity() < quantity) {
            throw new BadRequestException("Not enough inventory for skuCode: " + skuCode);
        }
        inventory.changeQuantity(inventory.getQuantity() - quantity);
        inventoryEntity.setQuantity(inventory.getQuantity());
        return inventory;
    }

    @Transactional(readOnly = true)
    public Integer getStock(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode).map(InventoryEntity::getQuantity)
                .orElseThrow(() -> new BadRequestException(inventoryNotFoundStr + skuCode));
    }
}
