package dev.swang.ecommerce.inventoryservice.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import dev.swang.ecommerce.inventoryservice.config.BadRequestException;
import dev.swang.ecommerce.inventoryservice.model.Inventory;
import dev.swang.ecommerce.inventoryservice.model.InventoryType;
import dev.swang.ecommerce.inventoryservice.persistence.InventoryEntity;
import dev.swang.ecommerce.inventoryservice.persistence.InventoryRepository;
import dev.swang.ecommerce.utils.InventoryMapper;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    public InventoryService(InventoryRepository inventoryRepository,
            InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @Transactional
    public Boolean ifExists(String sku, Integer location) {
        return inventoryRepository.findBySkuAndLocation(sku, location).isPresent();
    }

    @Transactional
    public Optional<Inventory> get(String sku, Integer location) {
        return inventoryRepository.findBySkuAndLocation(sku, location)
                .map(inventoryMapper::entityToInventory);
    }

    /**
     * Get a list of inventory for the given sku and location, with pagination.
     * 
     * @param sku
     * @param location
     * @param offset must be multiple of limit, otherwise will throw exception, must be 0, 10, 20,
     *        30, etc. when limit is 10
     * @param limit must be greater than 0, otherwise will throw exception
     * @return
     */
    @Transactional
    public List<Inventory> get(String sku, int offset, int limit) {

        if (limit <= 0) {
            throw new BadRequestException("Limit must be greater than 0");
        }
        if (offset < 0 || offset % limit != 0) {
            throw new BadRequestException(
                    "Offset must be a multiple of limit and greater than or equal to 0");
        }

        PageRequest pageable = PageRequest.of(offset / limit, limit);
        Page<InventoryEntity> page =
                StringUtils.hasText(sku) ? inventoryRepository.findBySku(sku, pageable)
                        : inventoryRepository.findAll(pageable);

        return page.getContent().stream().map(inventoryMapper::entityToInventory)
                .collect(Collectors.toList());
    }

    /**
     * Get a list of inventory for the given sku and location, with pagination.
     * 
     * @param sku
     * @param location
     * @param offset must be multiple of limit, otherwise will throw exception, must be 0, 10, 20,
     *        30, etc. when limit is 10
     * @param limit must be greater than 0, otherwise will throw exception
     * @return
     */
    @Transactional
    public List<Inventory> get(Integer location, int offset, int limit) {

        if (limit <= 0) {
            throw new BadRequestException("Limit must be greater than 0");
        }
        if (offset < 0 || offset % limit != 0) {
            throw new BadRequestException(
                    "Offset must be a multiple of limit and greater than or equal to 0");
        }

        PageRequest pageable = PageRequest.of(offset / limit, limit);
        Page<InventoryEntity> page =
                location != null ? inventoryRepository.findByLocation(location, pageable)
                        : inventoryRepository.findAll(pageable);

        return page.getContent().stream().map(inventoryMapper::entityToInventory)
                .collect(Collectors.toList());
    }



    /**
     * Create a new inventory of the type, if the sku/location already exists, throw the exception.
     * 
     * @param sku
     * @param location
     * @param quantity
     * @param type
     * @return
     */
    @Transactional(readOnly = false)
    public Inventory create(String sku, Integer location, Integer quantity, InventoryType type) {
        if (inventoryRepository.findBySkuAndLocation(sku, location).isPresent()) {
            throw new BadRequestException(
                    "Inventory already exists for sku: " + sku + " and location: " + location);
        }
        InventoryEntity entity = new InventoryEntity.Builder(UUID.randomUUID()).sku(sku)
                .location(location).type(type.name()).build();
        return inventoryMapper.entityToInventory(inventoryRepository.save(entity));
    }

    /**
     * Add the inventory of quantity to the existing one, throw exception if it doesn't exist.
     * 
     * @param sku
     * @param location
     * @param quantity
     * @return
     */
    @Transactional(readOnly = false)
    public Inventory enStock(String sku, Integer location, Integer quantity) {
        if (!inventoryRepository.findBySkuAndLocation(sku, location).isPresent()) {
            throw new BadRequestException(
                    "Inventory does not exist for sku: " + sku + " and location: " + location);
        }
        InventoryEntity inventoryEntity =
                inventoryRepository.findBySkuAndLocation(sku, location).get();
        inventoryEntity.setQuantity(inventoryEntity.getQuantity() + quantity);
        return inventoryMapper.entityToInventory(inventoryRepository.save(inventoryEntity));
    }

    /**
     * Remove the inventory for location of sku, throw exception if it doesn't exist or there is not
     * enough
     * 
     * @param sku
     * @param location
     * @param quantity
     * @return
     */
    @Transactional(readOnly = false)
    public Inventory outStock(String sku, Integer location, Integer quantity) {
        if (!inventoryRepository.findBySkuAndLocation(sku, location).isPresent()) {
            throw new BadRequestException(
                    "Inventory does not exist for sku: " + sku + " and location: " + location);
        }
        InventoryEntity inventoryEntity =
                inventoryRepository.findBySkuAndLocation(sku, location).get();
        if (inventoryEntity.getQuantity() < quantity) {
            throw new BadRequestException(
                    "Not enough inventory for sku: " + sku + " and location: " + location);
        }
        inventoryEntity.setQuantity(inventoryEntity.getQuantity() - quantity);
        return inventoryMapper.entityToInventory(inventoryRepository.save(inventoryEntity));
    }

    @Transactional(readOnly = false)
    public Inventory delete(String sku, Integer location) {
        if (!inventoryRepository.findBySkuAndLocation(sku, location).isPresent()) {
            throw new BadRequestException(
                    "Inventory does not exist for sku: " + sku + " and location: " + location);
        }
        InventoryEntity inventoryEntity =
                inventoryRepository.findBySkuAndLocation(sku, location).get();
        inventoryRepository.delete(inventoryEntity);
        return inventoryMapper.entityToInventory(inventoryEntity);
    }
}
