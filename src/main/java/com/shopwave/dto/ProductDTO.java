//ATE/2545/14
package com.shopwave.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductDTO {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer stock;
        private Long categoryId;
        private String categoryName;
        private LocalDateTime createdAt;
}
