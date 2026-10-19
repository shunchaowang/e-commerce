package dev.swang.ecommerce.inventoryservice.api;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import dev.swang.ecommerce.inventoryservice.config.BadRequestException;
import dev.swang.ecommerce.inventoryservice.model.Inventory;
import dev.swang.ecommerce.inventoryservice.model.InventoryType;
import dev.swang.ecommerce.inventoryservice.service.InventoryService;
import dev.swang.ecommerce.utils.InventoryMapper;

@WebMvcTest(InventoryController.class)
@Import({InventoryMapper.class})
class InventoryControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private InventoryService inventoryService;

        @Test
        void getInventory_WhenInventoryExists_GivenSkuAndLocation_ThenReturnInventory() throws Exception {
                String sku = "SKU123";
                int location = 899;
                int quantity = 10;

                when(inventoryService.get(sku, location)).thenReturn(Optional.of(new Inventory.Builder().sku(sku).location(location).quantity(quantity).build()));

                mockMvc.perform(get("/api/v1/inventory/" + sku + "/" + location)).andExpect(status().isOk())
                                .andExpect(jsonPath("$.sku").value(sku))
                                .andExpect(jsonPath("$.location").value(location))
                                .andExpect(jsonPath("$.quantity").value(quantity));

                verify(inventoryService).get(sku, location);
        }

        @Test
        void getInventory_WhenInventoryNotExists_GivenSkuAndLocation_ThenReturnNotFound() throws Exception {
                String sku = "SKU123";
                int location = 899;

                when(inventoryService.get(sku, location)).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/v1/inventory/" + sku + "/" + location)).andExpect(status().isNotFound());

                verify(inventoryService).get(sku, location);
        }

        @Test 
        void createInventory_WhenNotExisted_GivenValidRequest_ThenReturnCreated() throws Exception {
                String sku = "SKU123";
                int location = 899;
                int quantity = 10;
                String type = "STORE";

                Inventory inventory = new Inventory.Builder().sku(sku).location(location).quantity(quantity).type(InventoryType.valueOf(type)).build();

                when(inventoryService.create(inventory)).thenReturn(inventory);

                mockMvc.perform(post("/api/v1/inventory")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"sku\":\"" + sku + "\", \"location\":" + location + ", \"quantity\":" + quantity + ", \"type\":\"" + type + "\"}"))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.sku").value(sku))
                                .andExpect(jsonPath("$.location").value(location))
                                .andExpect(jsonPath("$.quantity").value(quantity));
        }

        @Test
        void enStock_WhenValidRequiredAndInventoryNotExists_ThenReturnNotFound() throws Exception {
                String sku = "SKU123";
                int quantity = 10;

                when(inventoryService.ifExists(sku, location)).thenReturn(false);

                mockMvc.perform(post("/api/v1/inventory/" + sku + "?valid=true")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"quantity\":" + quantity + "}"))
                                .andExpect(status().isNotFound());

                verify(inventoryService).ifExists(sku);
                verify(inventoryService, never()).enStock(sku, quantity);
        }

        @Test
        void enStock_WhenValidRequiredAndInventoryExists_ThenReturnCreated() throws Exception {
                String sku = "SKU123";
                int quantity = 10;

                when(inventoryService.getStock(skuCode))
                                .thenThrow(new BadRequestException("Inventory not found"));

                mockMvc.perform(get("/api/v1/inventory/" + skuCode)).andExpect(status().isNotFound());

                verify(inventoryService).getStock(skuCode);
        }

        @Test
        void enStock_WhenValidRequiredAndInventoryNotExists_ThenReturnNotFound() throws Exception {
                String skuCode = "SKU123";
                int quantity = 10;

                when(inventoryService.ifExists(skuCode)).thenReturn(false);

                mockMvc.perform(post("/api/v1/inventory/" + skuCode + "?valid=true")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"quantity\":" + quantity + "}"))
                                .andExpect(status().isNotFound());

                verify(inventoryService).ifExists(skuCode);
                verify(inventoryService, never()).enStock(skuCode, quantity);
        }

        @Test
        void enStock_WhenValidRequiredAndInventoryExists_ThenReturnCreated() throws Exception {
                String skuCode = "SKU123";
                int quantity = 10;

                when(inventoryService.ifExists(skuCode)).thenReturn(true);
                Inventory inventory = new Inventory.Builder().skuCode(skuCode).quantity(quantity).build();
                when(inventoryService.enStock(skuCode, quantity)).thenReturn(inventory);

                mockMvc.perform(post("/api/v1/inventory/" + skuCode + "?valid=true")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"quantity\":" + quantity + "}"))
                                .andExpect(status().isCreated()).andExpect(jsonPath("$.skuCode").value(skuCode))
                                .andExpect(jsonPath("$.quantity").value(quantity));

                verify(inventoryService).ifExists(skuCode);
                verify(inventoryService).enStock(skuCode, quantity);
        }

        @Test
        void enStock_WhenValidNotRequiredAndInventoryNotExists_ThenReturnCreated() throws Exception {
                String skuCode = "SKU123";
                int quantity = 10;

                Inventory inventory = new Inventory.Builder().skuCode(skuCode).quantity(quantity).build();
                when(inventoryService.enStock(skuCode, quantity)).thenReturn(inventory);

                mockMvc.perform(post("/api/v1/inventory/" + skuCode + "?valid=false")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"quantity\":" + quantity + "}"))
                                .andExpect(status().isCreated()).andExpect(jsonPath("$.skuCode").value(skuCode))
                                .andExpect(jsonPath("$.quantity").value(quantity));

                verify(inventoryService, never()).ifExists(skuCode);
                verify(inventoryService).enStock(skuCode, quantity);
        }

        @Test
        void outStock_WhenInventoryNotFound_ThenReturnBadRequest() throws Exception {
                String skuCode = "SKU123";
                int quantity = 10;

                when(inventoryService.outStock(skuCode, quantity))
                                .thenThrow(new BadRequestException("Inventory not found"));

                mockMvc.perform(put("/api/v1/inventory/" + skuCode).contentType(MediaType.APPLICATION_JSON)
                                .content("{\"quantity\":" + quantity + "}")).andExpect(status().isBadRequest());

                verify(inventoryService).outStock(skuCode, quantity);
        }

        @Test
        void outStock_WhenNotEnoughInventory_ThenReturnBadRequest() throws Exception {
                String skuCode = "SKU123";
                int quantity = 10;

                when(inventoryService.outStock(skuCode, quantity))
                                .thenThrow(new BadRequestException("Not enough inventory"));

                mockMvc.perform(put("/api/v1/inventory/" + skuCode).contentType(MediaType.APPLICATION_JSON)
                                .content("{\"quantity\":" + quantity + "}")).andExpect(status().isBadRequest());

                verify(inventoryService).outStock(skuCode, quantity);
        }

        @Test
        void outStock_WhenInventoryExists_ThenReturnOk() throws Exception {
                String skuCode = "SKU123";
                int quantity = 10;

                Inventory inventory = new Inventory.Builder().skuCode(skuCode).quantity(quantity).build();
                when(inventoryService.outStock(skuCode, quantity)).thenReturn(inventory);

                mockMvc.perform(put("/api/v1/inventory/" + skuCode).contentType(MediaType.APPLICATION_JSON)
                                .content("{\"quantity\":" + quantity + "}")).andExpect(status().isOk())
                                .andExpect(jsonPath("$.skuCode").value(skuCode))
                                .andExpect(jsonPath("$.quantity").value(quantity));

                verify(inventoryService).outStock(skuCode, quantity);
        }

}
