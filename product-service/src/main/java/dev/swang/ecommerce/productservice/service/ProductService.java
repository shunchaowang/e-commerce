package dev.swang.ecommerce.productservice.service;

import dev.swang.ecommerce.productservice.Repository.ProductRepository;
import dev.swang.ecommerce.productservice.dto.ProductRequest;
import dev.swang.ecommerce.productservice.dto.ProductResponse;
import dev.swang.ecommerce.productservice.model.Product;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

  private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Transactional
  public ProductResponse createProduct(ProductRequest productRequest) {
    Product product = new Product.Builder()
        .name(productRequest.name())
        .description(productRequest.description())
        .price(productRequest.price())
        .build();
    productRepository.save(product);
    logger.info("Product {} is created.", product.getId());
    return new ProductResponse(product.getId(), product.getName(), product.getDescription(),
        product.getPrice(), product.getCreatedAt(), product.getUpdatedAt());
  }

  @Transactional(readOnly = true)
  public List<ProductResponse> getAllProducts() {
    return productRepository.findAll().stream()
        .map(product -> new ProductResponse(product.getId(), product.getName(),
            product.getDescription(), product.getPrice(), product.getCreatedAt(),
            product.getUpdatedAt()))
        .toList();
  }

  @Transactional(readOnly = true)
  public ProductResponse getProductById(String id) {
    return productRepository.findById(id)
        .map(product -> new ProductResponse(product.getId(), product.getName(),
            product.getDescription(), product.getPrice(), product.getCreatedAt(),
            product.getUpdatedAt()))
        .orElseThrow(() -> new RuntimeException("Product not found"));
  }
}
