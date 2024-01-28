package com.bookstore.applicaton.mapper;

import com.bookstore.applicaton.domain.Product;
import com.bookstore.applicaton.dto.ProductDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")  // For integration with Spring
public interface ProductMapper {

    @Mapping(target = "productId", ignore = true)  // Ignore ID during mapping
    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);
}