//ATE/2545/14
package com.shopwave.Exception;

public class ProductNotFoundException extends RuntimeException{


    public ProductNotFoundException(Long id) {
        super("Product not found with id: " + id);
    }
}
