package com.bookstore.applicaton.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {

    private Integer productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantityAvailable;

    public Product(Integer productId, String name, String description, BigDecimal price, Integer quantityAvailable) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
    }

    public BigDecimal applyDiscount(BigDecimal discountPercentage) { // Changed to BigDecimal
        return this.price.multiply(BigDecimal.ONE.subtract(discountPercentage.divide(BigDecimal.valueOf(100))));
    }

    public BigDecimal applyTax(BigDecimal taxRate) { // Changed to BigDecimal
        return this.price.multiply(BigDecimal.ONE.add(taxRate.divide(BigDecimal.valueOf(100))));
    }

}