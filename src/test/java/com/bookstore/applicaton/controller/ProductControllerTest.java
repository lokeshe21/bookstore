package com.bookstore.applicaton.controller;

import com.bookstore.applicaton.common.ApiResponse;
import com.bookstore.applicaton.constants.ProductResponseMessages;
import com.bookstore.applicaton.dto.ProductDto;
import com.bookstore.applicaton.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_ValidProductDto_CreatedSuccessfully() {
        // Arrange
        ProductDto productDto = new ProductDto(1, "Test Product", "Test Description", BigDecimal.TEN, 100);
        ApiResponse<ProductDto> expectedResponse = new ApiResponse<>(HttpStatus.CREATED.value(), true,
                ProductResponseMessages.PRODUCT_CREATED_SUCCESSFULLY, productDto);

        // Mocking behavior
        when(productService.createProduct(productDto)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ApiResponse<ProductDto>> responseEntity = productController.createProduct(productDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedResponse, responseEntity.getBody());

        // Verify that the service method was called
        verify(productService, times(1)).createProduct(productDto);
    }

    @Test
    void getProduct_ExistingProductId_ProductRetrievedSuccessfully() {
        // Arrange
        int productId = 1;
        ProductDto productDto = new ProductDto(productId, "Test Product", "Test Description", BigDecimal.TEN, 100);
        ApiResponse<ProductDto> expectedResponse = new ApiResponse<>(HttpStatus.OK.value(), true,
                ProductResponseMessages.PRODUCT_RETRIEVED_SUCCESSFULLY, productDto);

        // Mocking behavior
        when(productService.getProduct(productId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ApiResponse<ProductDto>> responseEntity = productController.getProduct(productId);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedResponse, responseEntity.getBody());

        // Verify that the service method was called
        verify(productService, times(1)).getProduct(productId);
    }

    @Test
    void updateProduct_ValidProductIdAndProductDto_ProductUpdatedSuccessfully() {
        // Arrange
        int productId = 1;
        ProductDto productDto = new ProductDto(productId, "Updated Product", "Updated Description", BigDecimal.valueOf(20), 150);
        ApiResponse<ProductDto> expectedResponse = new ApiResponse<>(HttpStatus.OK.value(), true,
                ProductResponseMessages.PRODUCT_UPDATED_SUCCESSFULLY, productDto);

        // Mocking behavior
        when(productService.updateProduct(productId, productDto)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ApiResponse<ProductDto>> responseEntity = productController.updateProduct(productId, productDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedResponse, responseEntity.getBody());

        // Verify that the service method was called
        verify(productService, times(1)).updateProduct(productId, productDto);
    }

    @Test
    void deleteProduct_ExistingProductId_ProductDeletedSuccessfully() {
        // Arrange
        int productId = 1;
        ProductDto productDto = new ProductDto(productId, "Deleted Product", "Deleted Description", BigDecimal.valueOf(30), 200);
        ApiResponse<ProductDto> expectedResponse = new ApiResponse<>(HttpStatus.OK.value(), true,
                ProductResponseMessages.PRODUCT_DELETED_SUCCESSFULLY, productDto);

        // Mocking behavior
        when(productService.deleteProduct(productId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ApiResponse<ProductDto>> responseEntity = productController.deleteProduct(productId);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedResponse, responseEntity.getBody());

        // Verify that the service method was called
        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    void applyDiscountOrTax_ValidProductIdDiscountTypeAndValue_DiscountAppliedSuccessfully() {
        // Arrange
        int productId = 1;
        BigDecimal discountValue = BigDecimal.TEN;
        ProductDto productDto = new ProductDto(productId, "Test Product", "Test Description", BigDecimal.valueOf(40), 250);
        ApiResponse<ProductDto> expectedResponse = new ApiResponse<>(HttpStatus.OK.value(), true,
                ProductResponseMessages.OPERATION_APPLIED_SUCCESSFULLY, productDto);

        // Mocking behavior
        when(productService.applyDiscountOrTax(productId, "discount", discountValue)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ApiResponse<ProductDto>> responseEntity = productController.applyDiscountOrTax(productId, "discount", discountValue);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedResponse, responseEntity.getBody());

        // Verify that the service method was called
        verify(productService, times(1)).applyDiscountOrTax(productId, "discount", discountValue);
    }

    @Test
    void getAllProducts_ProductsExist_AllProductsRetrievedSuccessfully() {
        // Arrange
        List<ProductDto> allProductsDto = Arrays.asList(
                new ProductDto(1, "Product 1", "Description 1", BigDecimal.valueOf(50), 300),
                new ProductDto(2, "Product 2", "Description 2", BigDecimal.valueOf(60), 400)
        );
        ApiResponse<List<ProductDto>> expectedResponse = new ApiResponse<>(HttpStatus.OK.value(), true,
                ProductResponseMessages.ALL_PRODUCTS_RETRIEVED_SUCCESSFULLY, allProductsDto);

        // Mocking behavior
        when(productService.getAllProducts()).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ApiResponse<List<ProductDto>>> responseEntity = productController.getAllProducts();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedResponse, responseEntity.getBody());

        // Verify that the service method was called
        verify(productService, times(1)).getAllProducts();
    }
}