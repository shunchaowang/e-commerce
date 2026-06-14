package dev.swang.ecommerce.productservice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
// run test container on a random port so it doesn't conflict with actual service port on 8080
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

  // this variable will be injected with the random port when the test container is started
  @LocalServerPort
  private int port;

  @BeforeEach
  void setup() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

  @Test
  void shouldCreateProduct() {

    String requestBody = """
        {
            "name": "test product",
            "description": "test description",
            "price": 100
        }
        """;

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .when()
        .post("/api/v1/products")
        .then()
        .statusCode(201)
        .body("id", Matchers.notNullValue())
        .body("name", Matchers.equalTo("test product"))
        .body("description", Matchers.equalTo("test description"))
        .body("price", Matchers.equalTo(100));
  }

  @Test
  void shouldGetProductById() {
    String requestBody = """
        {
            "name": "test product",
            "description": "test description",
            "price": 100
        }
        """;

    String id = RestAssured.given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .when()
        .post("/api/v1/products")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getString("id");

    RestAssured.when()
        .get("/api/v1/products/{id}", id)
        .then()
        .statusCode(200)
        .body("id", Matchers.equalTo(id))
        .body("name", Matchers.equalTo("test product"))
        .body("description", Matchers.equalTo("test description"))
        .body("price", Matchers.equalTo(100));
  }

  @Test
  void shouldGetAllProducts() {

    int size = 10;

    for (int i = 0; i < size; i++) {
      String requestBody = """
          {
            "name": "test product %s",
            "description": "test description %s",
            "price": %d
          }
          """.formatted(i, i, i + 100);

      RestAssured.given()
          .contentType(ContentType.JSON)
          .body(requestBody)
          .when()
          .post("/api/v1/products")
          .then()
          .statusCode(201);

    }

    RestAssured.when()
        .get("/api/v1/products")
        .then()
        .statusCode(200)
        .body("size()", Matchers.is(size));

  }

}
