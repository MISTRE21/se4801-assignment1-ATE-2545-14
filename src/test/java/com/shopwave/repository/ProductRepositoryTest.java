//ATE/2545/14

package com.shopwave.repository;

import com.shopwave.model.Category;
import com.shopwave.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findByNameContainingIgnoreCase_shouldReturnMatchingProducts() {
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