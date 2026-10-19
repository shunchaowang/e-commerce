package dev.swang.ecommerce.inventoryservice.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.swang.ecommerce.inventoryservice.config.BadRequestException;
import dev.swang.ecommerce.inventoryservice.model.Inventory;
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
    public Boolean ifExists(String sku, int location) {
        return inventoryRepository.findBySkuAndLocation(sku, location).isPresent();
    }

    @Transactional
    public Optional<Inventory> get(String sku, int location) {
        return inventoryRepository.findBySkuAndLocation(sku, location)
                .map(inventoryMapper::entityToInventory);
    }

    /**
     * Get all inventory with pagination.
     * 
     * @param offset must be multiple of limit, otherwise will throw exception, must be 0, 10, 20,
     *        30, etc. when limit is 10
     * @param limit must be greater than 0, otherwise will throw exception
     * @return
     */
    @Transactional
    public List<Inventory> getAll(int offset, int limit) {
        if (limit == -1 && offset == -1) {
            return inventoryRepository.findAll().stream().map(inventoryMapper::entityToInventory)
                    .collect(Collectors.toList());
        }


        if (offset % limit != 0) {
            throw new BadRequestException(
                    "Offset must be a multiple of limit and greater than or equal to 0");
        }

        PageRequest pageable = PageRequest.of(offset / limit, limit);
        Page<InventoryEntity> page = inventoryRepository.findAll(pageable);
        return page.getContent().stream().map(inventoryMapper::entityToInventory)
                .collect(Collectors.toList());
    }


    /**
     * Create a new inventory of the type, if the sku/location already exists, throw the exception.
     * 
     * @param inventory
     * @return
     */
    @Transactional(readOnly = false)
    public Inventory create(Inventory inventory) {
        if (inventoryRepository.findBySkuAndLocation(inventory.getSku(), inventory.getLocation())
                .isPresent()) {
            throw new BadRequestException("Inventory already exists for sku: " + inventory.getSku()
                    + " and location: " + inventory.getLocation());
        }
        InventoryEntity entity =
                new InventoryEntity.Builder(UUID.randomUUID()).sku(inventory.getSku())
                        .location(inventory.getLocation()).type(inventory.getType().name()).build();
        return inventoryMapper.entityToInventory(inventoryRepository.save(entity));
    }

    @Transactional(readOnly = false)
    public Inventory update(Inventory inventory) {
        if (!inventoryRepository.findBySkuAndLocation(inventory.getSku(), inventory.getLocation())
                .isPresent()) {
            throw new BadRequestException("Inventory does not exist for sku: " + inventory.getSku()
                    + " and location: " + inventory.getLocation());
        }
        InventoryEntity entity = inventoryMapper.inventoryToEntity(inventory);
        return inventoryMapper.entityToInventory(inventoryRepository.save(entity));
    }

    @Transactional(readOnly = false)
    public Inventory delete(String sku, int location) {
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
