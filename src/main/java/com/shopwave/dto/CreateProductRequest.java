//ATE/2545/14
package com.shopwave.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;


}