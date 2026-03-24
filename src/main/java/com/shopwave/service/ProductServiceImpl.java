//ATE/2545/14
package com.shopwave.service;

import com.shopwave.Exception.ProductNotFoundException;
import com.shopwave.dto.CreateProductRequest;
import com.shopwave.dto.ProductDTO;
import com.shopwave.mapper.ProductMapper;
import com.shopwave.model.Category;
import com.shopwave.model.Product;
import com.shopwave.repository.CategoryRepository;
import com.shopwave.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;


    @Override
    public ProductDTO createProduct(CreateProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(category)
                .build();

        return productMapper.toDTO(productRepository.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(""));
        return productMapper.toDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String keyword, BigDecimal maxPrice) {

        List<Product> products = productRepository
                .findByNameContainingIgnoreCase(keyword);

        return products.stream()
                .filter(product -> product.getPrice().compareTo(maxPrice) <= 0)
                .map(productMapper::toDTO)
                .toList();
    }

    @Override
    public ProductDTO updateStock(Long id, int delta) {
        Product product = productRepository.findById(id)
                .orElseThrow();
        int newStock = product.getStock() + delta;

        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        product.setStock(newStock);
        return productMapper.toDTO(product);
    }


}