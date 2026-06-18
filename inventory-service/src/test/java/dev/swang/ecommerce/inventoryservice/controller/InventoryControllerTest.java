package dev.swang.ecommerce.inventoryservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

  @Autowired
  private MockMvc mockMvc;
}