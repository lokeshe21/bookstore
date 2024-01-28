package com.bookstore.applicaton.controller;

import com.bookstore.applicaton.common.ApiResponse;
import com.bookstore.applicaton.constants.ProductResponseMessages;
import com.bookstore.applicaton.dto.ProductDto;
import com.bookstore.applicaton.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",
                    description =  ProductResponseMessages.PRODUCT_CREATED_SUCCESSFULLY),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = ProductResponseMessages.BAD_REQUEST)
    })
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@Valid @RequestBody ProductDto productDto) {
        ApiResponse<ProductDto> response = productService.createProduct(productDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Retrieve a product by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = ProductResponseMessages.PRODUCT_RETRIEVED_SUCCESSFULLY),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = ProductResponseMessages.PRODUCT_NOT_FOUND)
    })
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(@PathVariable (required = true, value = "productId") Integer productId) {
        ApiResponse<ProductDto> response = productService.getProduct(productId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update an existing product")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = ProductResponseMessages.PRODUCT_UPDATED_SUCCESSFULLY),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = ProductResponseMessages.PRODUCT_NOT_FOUND)
    })
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @PathVariable (required = true, value = "productId") Integer productId,
            @Valid @RequestBody ProductDto productDto) {
        productDto.setProductId(productId); // Ensure ID consistency
        ApiResponse<ProductDto> response = productService.updateProduct(productId, productDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete a product")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = ProductResponseMessages.PRODUCT_DELETED_SUCCESSFULLY),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = ProductResponseMessages.PRODUCT_NOT_FOUND)
    })
    public ResponseEntity<ApiResponse<ProductDto>> deleteProduct(
            @PathVariable (required = true, value = "productId") Integer productId) {
        ApiResponse<ProductDto> response = productService.deleteProduct(productId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PutMapping("/{productId}/{type}")
    @Operation(summary = "Apply discount or tax to a product")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = ProductResponseMessages.OPERATION_APPLIED_SUCCESSFULLY),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = ProductResponseMessages.PRODUCT_NOT_FOUND),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = ProductResponseMessages.INVALID_OPERATION_TYPE)
    })
    public ResponseEntity<ApiResponse<ProductDto>> applyDiscountOrTax(
            @PathVariable (required = true, value = "productId") Integer productId,
            @PathVariable  (required = true, value = "type")   String type, @RequestParam
                    (required = true, value = "percentageValue") BigDecimal value) {
        ApiResponse<ProductDto> response = productService.applyDiscountOrTax(productId, type, value);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping
    @Operation(summary = "Retrieve all products")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = ProductResponseMessages.ALL_PRODUCTS_RETRIEVED_SUCCESSFULLY),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = ProductResponseMessages.NO_PRODUCTS_FOUND)
    })
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        ApiResponse<List<ProductDto>> response = productService.getAllProducts();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
}
