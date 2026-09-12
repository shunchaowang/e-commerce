package dev.swang.ecommerce.inventoryservice;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import dev.swang.ecommerce.inventoryservice.Inventory;
import dev.swang.ecommerce.inventoryservice.InventoryController;
import dev.swang.ecommerce.inventoryservice.InventoryService;
import dev.swang.ecommerce.inventoryservice.config.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService inventoryService;

    @Test
    void isInStock_WhenInventoryExists_ThenReturnTrue() throws Exception {
        String skuCode = "SKU123";
        int quantity = 10;

        when(inventoryService.inStock(skuCode, quantity)).thenReturn(true);

        mockMvc.perform(get("/api/v1/inventory").param("skuCode", skuCode).param("quantity",
                String.valueOf(quantity))).andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void isInStock_WhenInventoryNotExists_ThenReturnFalse() throws Exception {
        String skuCode = "SKU123";
        int quantity = 10;

        when(inventoryService.inStock(skuCode, quantity)).thenReturn(false);

        mockMvc.perform(get("/api/v1/inventory").param("skuCode", skuCode).param("quantity",
                String.valueOf(quantity))).andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }

    @Test
    void enStock_WhenValidRequest_ThenReturnCreated() throws Exception {
        String skuCode = "SKU123";
        int quantity = 10;

        Inventory inventory = new Inventory.Builder().skuCode(skuCode).quantity(quantity).build();

        when(inventoryService.enStock(skuCode, quantity)).thenReturn(inventory);

        mockMvc.perform(post("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON)
                .content("{\"skuCode\":\"" + skuCode + "\",\"quantity\":" + quantity + "}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.skuCode").value(skuCode))
                .andExpect(jsonPath("$.quantity").value(quantity));
    }

    @Test
    void outStock_WhenValidRequest_ThenReturnOk() throws Exception {
        String skuCode = "SKU123";
        int quantity = 10;

        Inventory inventory = new Inventory.Builder().skuCode(skuCode).quantity(quantity).build();

        when(inventoryService.outStock(skuCode, quantity)).thenReturn(inventory);

        mockMvc.perform(put("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON)
                .content("{\"skuCode\":\"" + skuCode + "\",\"quantity\":" + quantity + "}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.skuCode").value(skuCode))
                .andExpect(jsonPath("$.quantity").value(quantity));
    }

    @Test
    void outStock_WhenInventoryNotFound_ThenReturnBadRequest() throws Exception {
        String skuCode = "SKU123";
        int quantity = 10;

        when(inventoryService.outStock(skuCode, quantity))
                .thenThrow(new BadRequestException("Inventory not found"));

        mockMvc.perform(put("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON)
                .content("{\"skuCode\":\"" + skuCode + "\",\"quantity\":" + quantity + "}"))
                .andExpect(status().isBadRequest());
    }
}
