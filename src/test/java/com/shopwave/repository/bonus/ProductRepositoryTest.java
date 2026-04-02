//ATE/2545/14
package com.shopwave.repository.bonus;

import com.shopwave.model.Category;
import com.shopwave.model.Product;
import com.shopwave.repository.CategoryRepository;
import com.shopwave.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findByNameContainingIgnoreCase_shouldReturnCorrectResults() {
        Category category = categoryRepository.save(
                Category.builder()
                        .name("Electronics")
                        .description("Electronic items")
                        .build()
        );

        productRepository.save(Product.builder()
                .name("Laptop")
                .description("Business laptop")
                .price(new BigDecimal("1299.99"))
                .stock(10)
                .category(category)
                .build());

        productRepository.save(Product.builder()
                .name("Gaming Laptop")
                .description("High performance laptop")
                .price(new BigDecimal("1999.99"))
                .stock(5)
                .category(category)
                .build());

        productRepository.save(Product.builder()
                .name("Mouse")
                .description("Wireless mouse")
                .price(new BigDecimal("25.00"))
                .stock(50)
                .category(category)
                .build());

        List<Product> results = productRepository.findByNameContainingIgnoreCase("lap");

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("Laptop")));
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("Gaming Laptop")));
        assertFalse(results.stream().anyMatch(p -> p.getName().equals("Mouse")));
    }
}
