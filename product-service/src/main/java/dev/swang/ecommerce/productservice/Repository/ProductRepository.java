package dev.swang.ecommerce.productservice.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import dev.swang.ecommerce.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
