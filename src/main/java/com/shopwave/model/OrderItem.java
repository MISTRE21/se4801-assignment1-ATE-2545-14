//ATE/2545/14
package com.shopwave.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "product_id", nullable = false)
     private Product product;

     @Column(nullable = false)
     private Integer Quantity;

     @Column(nullable = false, precision = 12, scale = 2)
     private BigDecimal unitPrice;
     
     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "order_id")
     private Order order;

}
