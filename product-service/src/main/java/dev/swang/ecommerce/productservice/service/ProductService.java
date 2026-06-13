package dev.swang.ecommerce.productservice.service;

import dev.swang.ecommerce.productservice.Repository.ProductRepository;
import dev.swang.ecommerce.productservice.dto.ProductRequest;
import dev.swang.ecommerce.productservice.dto.ProductResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public ProductResponse createProduct(ProductRequest productRequest) {
    return null;
  }

  public List<ProductResponse> getProductById(String id) {
    return null;
  }
}
