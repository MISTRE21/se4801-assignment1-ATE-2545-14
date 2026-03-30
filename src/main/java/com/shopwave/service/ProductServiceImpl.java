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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
                .orElseThrow(() -> new EntityNotFoundException(
                        "Category not found with id: " + request.getCategoryId()
                ));

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
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String keyword, BigDecimal maxPrice) {
        boolean hasKeyword = keyword != null && !keyword.isBlank();
        boolean hasMaxPrice = maxPrice != null;

        if (hasKeyword && hasMaxPrice) {
            Map<Long, Product> filtered = new LinkedHashMap<>();

            productRepository.findByNameContainingIgnoreCase(keyword)
                    .stream()
                    .filter(p -> p.getPrice() != null && p.getPrice().compareTo(maxPrice) <= 0)
                    .forEach(p -> filtered.put(p.getId(), p));

            return filtered.values().stream()
                    .map(productMapper::toDTO)
                    .toList();
        }

        if (hasKeyword) {
            return productRepository.findByNameContainingIgnoreCase(keyword)
                    .stream()
                    .map(productMapper::toDTO)
                    .toList();
        }

        if (hasMaxPrice) {
            return productRepository.findByPriceLessThanEqual(maxPrice)
                    .stream()
                    .map(productMapper::toDTO)
                    .toList();
        }

        return List.of();
    }

    @Override
    public ProductDTO updateStock(Long id, int delta) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        int currentStock = product.getStock() == null ? 0 : product.getStock();
        int finalStock = currentStock + delta;

        if (finalStock < 0) {
            throw new IllegalArgumentException(
                    "Stock cannot go negative. Current stock: " + currentStock + ", delta: " + delta
            );
        }

        product.setStock(finalStock);
        return productMapper.toDTO(productRepository.save(product));
    }
}