package com.bookstore.applicaton.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Integer productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantityAvailable;

}