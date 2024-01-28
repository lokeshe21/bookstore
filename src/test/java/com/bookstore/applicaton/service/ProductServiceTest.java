package com.bookstore.applicaton.service;

import com.bookstore.applicaton.common.ApiResponse;
import com.bookstore.applicaton.constants.ProductResponseMessages;
import com.bookstore.applicaton.domain.Product;
import com.bookstore.applicaton.dto.ProductDto;
import com.bookstore.applicaton.mapper.ProductMapper;
import com.bookstore.applicaton.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_Success() {
        // Arrange
        ProductDto productDto = new ProductDto(null, "Test Product", "Description", BigDecimal.TEN, 50);
        int generatedProductId = 1;

        // Mocking behavior
        when(productRepository.addProduct(any(Product.class)))
                .thenReturn(new Product(generatedProductId, "Test Product", "Description", BigDecimal.TEN, 50));
        when(productMapper.toDto(any(Product.class)))
                .thenReturn(new ProductDto(generatedProductId, "Test Product", "Description", BigDecimal.TEN, 50));

        // Act
        ApiResponse<ProductDto> response = productService.createProduct(productDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(ProductResponseMessages.PRODUCT_CREATED_SUCCESSFULLY, response.getMessage());

        ProductDto createdProductDto = response.getData();
        assertNotNull(createdProductDto);
        assertEquals(generatedProductId, createdProductDto.getProductId());
        assertEquals("Test Product", createdProductDto.getName());
        assertEquals("Description", createdProductDto.getDescription());
        assertEquals(BigDecimal.TEN, createdProductDto.getPrice());
        assertEquals(50, createdProductDto.getQuantityAvailable());
    }

    @Test
    void createProduct_Failure() {
        // Arrange
        ProductDto productDto = new ProductDto(null, null, null, null, null);

        // Mocking behavior
        when(productRepository.addProduct(any(Product.class))).thenThrow(new RuntimeException("Invalid data"));

        // Act
        ApiResponse<ProductDto> response = productService.createProduct(productDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals("Invalid data", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void getProduct_ProductFound() {
        // Arrange
        int productId = 1;

        // Mocking behavior
        when(productRepository.getProduct(productId))
                .thenReturn(new Product(productId, "Test Product", "Description", BigDecimal.TEN, 50));
        when(productMapper.toDto(any(Product.class)))
                .thenReturn(new ProductDto(productId, "Test Product", "Description", BigDecimal.TEN, 50));

        // Act
        ApiResponse<ProductDto> response = productService.getProduct(productId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(ProductResponseMessages.PRODUCT_RETRIEVED_SUCCESSFULLY, response.getMessage());

        ProductDto retrievedProductDto = response.getData();
        assertNotNull(retrievedProductDto);
        assertEquals(productId, retrievedProductDto.getProductId());
        assertEquals("Test Product", retrievedProductDto.getName());
        assertEquals("Description", retrievedProductDto.getDescription());
        assertEquals(BigDecimal.TEN, retrievedProductDto.getPrice());
        assertEquals(50, retrievedProductDto.getQuantityAvailable());
    }

    @Test
    void getProduct_ProductNotFound() {
        // Arrange
        int productId = 1;

        // Mocking behavior
        when(productRepository.getProduct(productId)).thenReturn(null);

        // Act
        ApiResponse<ProductDto> response = productService.getProduct(productId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        assertEquals(ProductResponseMessages.PRODUCT_NOT_FOUND, response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void updateProduct_ProductExists_SuccessfullyUpdated() {
        // Arrange
        int productId = 1;
        ProductDto updatedProductDto = new ProductDto(productId, "Updated Product", "Updated Description",
                BigDecimal.valueOf(20.0), 100);

        // Mocking behavior
        when(productRepository.getProduct(productId))
                .thenReturn(new Product(productId, "Existing Product", "Description",
                        BigDecimal.TEN, 50));
        when(productMapper.toEntity(updatedProductDto))
                .thenReturn(new Product(productId, "Updated Product", "Updated Description",
                        BigDecimal.valueOf(20.0), 100));
        when(productRepository.updateProduct(productId, new Product(productId, "Updated Product",
                "Updated Description", BigDecimal.valueOf(20.0), 100)))
                .thenReturn(true);
        when(productMapper.toDto(any(Product.class)))
                .thenReturn(updatedProductDto);

        // Act
        ApiResponse<ProductDto> response = productService.updateProduct(productId, updatedProductDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(ProductResponseMessages.PRODUCT_UPDATED_SUCCESSFULLY, response.getMessage());

        ProductDto returnedProductDto = response.getData();
        assertNotNull(returnedProductDto);
        assertEquals(productId, returnedProductDto.getProductId());
        assertEquals("Updated Product", returnedProductDto.getName());
        assertEquals("Updated Description", returnedProductDto.getDescription());
        assertEquals(BigDecimal.valueOf(20.0), returnedProductDto.getPrice());
        assertEquals(100, returnedProductDto.getQuantityAvailable());
    }

    @Test
    void updateProduct_ProductNotExists_NotFound() {
        // Arrange
        int productId = 1;
        ProductDto updatedProductDto = new ProductDto(productId, "Updated Product", "Updated Description",
                BigDecimal.valueOf(20.0), 100);

        // Mocking behavior
        when(productRepository.getProduct(productId)).thenReturn(null);

        // Act
        ApiResponse<ProductDto> response = productService.updateProduct(productId, updatedProductDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        assertEquals(ProductResponseMessages.PRODUCT_NOT_FOUND, response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void updateProduct_FailedToUpdate_InternalServerError() {
        // Arrange
        int productId = 1;
        ProductDto updatedProductDto = new ProductDto(productId, "Updated Product", "Updated Description",
                BigDecimal.valueOf(20.0), 100);

        // Mocking behavior
        when(productRepository.getProduct(productId))
                .thenReturn(new Product(productId, "Existing Product", "Description",
                        BigDecimal.TEN, 50));
        when(productMapper.toEntity(updatedProductDto))
                .thenReturn(new Product(productId, "Updated Product", "Updated Description",
                        BigDecimal.valueOf(20.0), 100));
        when(productRepository.updateProduct(productId, new Product(productId, "Updated Product",
                "Updated Description", BigDecimal.valueOf(20.0), 100)))
                .thenReturn(false);

        // Act
        ApiResponse<ProductDto> response = productService.updateProduct(productId, updatedProductDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
        assertEquals(ProductResponseMessages.FAILED_TO_UPDATE_PRODUCT, response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void deleteProduct_ProductExists_SuccessfullyDeleted() {
        // Arrange
        int productId = 1;
        ProductDto deletedProductDto = new ProductDto(productId, "Deleted Product", "Deleted Description",
                BigDecimal.valueOf(15.0), 75);

        // Mocking behavior
        when(productRepository.getProduct(productId))
                .thenReturn(new Product(productId, "Deleted Product", "Deleted Description",
                        BigDecimal.valueOf(15.0), 75));
        when(productRepository.deleteProduct(productId)).thenReturn(true);
        when(productMapper.toDto(any(Product.class)))
                .thenReturn(deletedProductDto);

        // Act
        ApiResponse<ProductDto> response = productService.deleteProduct(productId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(ProductResponseMessages.PRODUCT_DELETED_SUCCESSFULLY, response.getMessage());

        ProductDto returnedProductDto = response.getData();
        assertNotNull(returnedProductDto);
        assertEquals(productId, returnedProductDto.getProductId());
        assertEquals("Deleted Product", returnedProductDto.getName());
        assertEquals("Deleted Description", returnedProductDto.getDescription());
        assertEquals(BigDecimal.valueOf(15.0), returnedProductDto.getPrice());
        assertEquals(75, returnedProductDto.getQuantityAvailable());
    }

    @Test
    void deleteProduct_ProductNotExists_NotFound() {
        // Arrange
        int productId = 1;

        // Mocking behavior
        when(productRepository.getProduct(productId)).thenReturn(null);

        // Act
        ApiResponse<ProductDto> response = productService.deleteProduct(productId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        assertEquals(ProductResponseMessages.PRODUCT_NOT_FOUND, response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void deleteProduct_FailedToDelete_InternalServerError() {
        // Arrange
        int productId = 1;

        // Mocking behavior
        when(productRepository.getProduct(productId))
                .thenReturn(new Product(productId, "Existing Product", "Description",
                        BigDecimal.TEN, 50));
        when(productRepository.deleteProduct(productId)).thenReturn(false);

        // Act
        ApiResponse<ProductDto> response = productService.deleteProduct(productId);

        // Assert
        assertNotNull(response);
        assertNull(response.getData());
    }

    @Test
    void applyDiscountOrTax_DiscountOperation_SuccessfullyApplied() {
        // Arrange
        int productId = 1;
        BigDecimal value = BigDecimal.valueOf(10.0);
        Product existingProduct = new Product(productId, "Existing Product", "Description",
                BigDecimal.valueOf(50.0), 100);
        ProductDto productDto = new ProductDto();
        productDto.setProductId(productId);
        productDto.setName("Existing Product");
        productDto.setDescription("Description");
        productDto.setPrice(BigDecimal.valueOf(45.0));
        productDto.setQuantityAvailable(100);

        // Mocking behavior
        when(productRepository.getProduct(productId)).thenReturn(existingProduct);
        when(productRepository.updateProduct(productId, existingProduct)).thenReturn(true);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        // Act
        ApiResponse<ProductDto> response = productService.applyDiscountOrTax(productId, "discount", value);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(ProductResponseMessages.OPERATION_APPLIED_SUCCESSFULLY, response.getMessage());

        ProductDto updatedProductDto = response.getData();
        assertNotNull(updatedProductDto);
        assertEquals(productId, updatedProductDto.getProductId());
        assertEquals("Existing Product", updatedProductDto.getName());
        assertEquals("Description", updatedProductDto.getDescription());
        assertEquals(BigDecimal.valueOf(45.0), updatedProductDto.getPrice());
        assertEquals(100, updatedProductDto.getQuantityAvailable());
    }

    @Test
    void applyDiscountOrTax_TaxOperation_SuccessfullyApplied() {
        // Arrange
        int productId = 1;
        BigDecimal value = BigDecimal.valueOf(10.0);
        Product existingProduct = new Product(productId, "Existing Product", "Description",
                BigDecimal.valueOf(50.0), 100);
        ProductDto productDto = new ProductDto();
        productDto.setProductId(productId);
        productDto.setName("Existing Product");
        productDto.setDescription("Description");
        productDto.setPrice(BigDecimal.valueOf(55.0));
        productDto.setQuantityAvailable(100);
        // Mocking behavior
        when(productRepository.getProduct(productId)).thenReturn(existingProduct);
        when(productRepository.updateProduct(productId, existingProduct)).thenReturn(true);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        // Act
        ApiResponse<ProductDto> response = productService.applyDiscountOrTax(productId, "tax", value);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(ProductResponseMessages.OPERATION_APPLIED_SUCCESSFULLY, response.getMessage());

        ProductDto updatedProductDto = response.getData();
        assertNotNull(updatedProductDto);
        assertEquals(productId, updatedProductDto.getProductId());
        assertEquals("Existing Product", updatedProductDto.getName());
        assertEquals("Description", updatedProductDto.getDescription());
        assertEquals(BigDecimal.valueOf(55.0), updatedProductDto.getPrice());
        assertEquals(100, updatedProductDto.getQuantityAvailable());
    }

    @Test
    void applyDiscountOrTax_InvalidOperation_InvalidOperationType() {
        // Arrange
        int productId = 1;
        BigDecimal value = BigDecimal.valueOf(10.0);

        // Mocking behavior
        when(productRepository.getProduct(productId)).thenReturn(new Product(productId, "Existing Product", "Description",
                BigDecimal.valueOf(50.0), 100));

        // Act
        ApiResponse<ProductDto> response = productService.applyDiscountOrTax(productId, "invalid", value);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(ProductResponseMessages.INVALID_OPERATION_TYPE, response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void applyDiscountOrTax_ProductNotFound_NotFound() {
        // Arrange
        int productId = 1;
        BigDecimal value = BigDecimal.valueOf(10.0);

        // Mocking behavior
        when(productRepository.getProduct(productId)).thenReturn(null);

        // Act
        ApiResponse<ProductDto> response = productService.applyDiscountOrTax(productId, "discount", value);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        assertEquals(ProductResponseMessages.PRODUCT_NOT_FOUND, response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void applyDiscountOrTax_FailedToUpdate_InternalServerError() {
        // Arrange
        int productId = 1;
        BigDecimal value = BigDecimal.valueOf(10.0);

        // Mocking behavior
        when(productRepository.getProduct(productId)).thenReturn(new Product(productId, "Existing Product", "Description",
                BigDecimal.valueOf(50.0), 100));
        when(productRepository.updateProduct(any(), any(Product.class))).thenReturn(false);

        // Act
        ApiResponse<ProductDto> response = productService.applyDiscountOrTax(productId, "discount", value);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
        assertEquals(ProductResponseMessages.FAILED_TO_UPDATE_PRODUCT, response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void applyDiscountOrTax_ExceptionThrown_BadRequest() {
        // Arrange
        int productId = 1;
        BigDecimal value = BigDecimal.valueOf(10.0);

        // Mocking behavior
        when(productRepository.getProduct(productId)).thenReturn(new Product(productId, "Existing Product", "Description",
                BigDecimal.valueOf(50.0), 100));
        when(productRepository.updateProduct(any(), any(Product.class))).thenThrow(new RuntimeException("Error"));

        // Act
        ApiResponse<ProductDto> response = productService.applyDiscountOrTax(productId, "discount", value);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertNotNull(response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void getAllProducts_ProductsExist_SuccessfullyRetrieved() {
        // Arrange
        List<Product> mockProducts = Arrays.asList(
                new Product(1, "Product 1", "Description 1",
                        BigDecimal.valueOf(10.0), 50),
                new Product(2, "Product 2", "Description 2",
                        BigDecimal.valueOf(20.0), 30)
        );

        ProductDto productDto1 = new ProductDto();
        productDto1.setProductId(1);
        productDto1.setName("Product 1");
        productDto1.setDescription("Description 1");
        productDto1.setPrice(BigDecimal.valueOf(10.0));
        productDto1.setQuantityAvailable(50);

        ProductDto productDto2 = new ProductDto();
        productDto2.setProductId(2);
        productDto2.setName("Product 2");
        productDto2.setDescription("Description 2");
        productDto2.setPrice(BigDecimal.valueOf(20.0));
        productDto2.setQuantityAvailable(30);

        List<ProductDto> mockProductsDto = Arrays.asList(productDto1,productDto2);

        // Mocking behavior
        when(productRepository.getAllProducts()).thenReturn(mockProducts);
        when(productMapper.toDto(mockProducts.get(0))).thenReturn(mockProductsDto.get(0));
        when(productMapper.toDto(mockProducts.get(1))).thenReturn(mockProductsDto.get(1));

        // Act
        ApiResponse<List<ProductDto>> response = productService.getAllProducts();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(ProductResponseMessages.ALL_PRODUCTS_RETRIEVED_SUCCESSFULLY, response.getMessage());

        List<ProductDto> retrievedProducts = response.getData();
        assertNotNull(retrievedProducts);
        assertEquals(2, retrievedProducts.size());

        ProductDto product1Dto = retrievedProducts.get(0);
        assertEquals(1, product1Dto.getProductId());
        assertEquals("Product 1", product1Dto.getName());
        assertEquals("Description 1", product1Dto.getDescription());
        assertEquals(10.0, product1Dto.getPrice().doubleValue());
        assertEquals(50, product1Dto.getQuantityAvailable());

        ProductDto product2Dto = retrievedProducts.get(1);
        assertEquals(2, product2Dto.getProductId());
        assertEquals("Product 2", product2Dto.getName());
        assertEquals("Description 2", product2Dto.getDescription());
        assertEquals(20.0, product2Dto.getPrice().doubleValue());
        assertEquals(30, product2Dto.getQuantityAvailable());
    }

    @Test
    void getAllProducts_NoProductsFound_ReturnsNotFound() {
        // Arrange
        // Mocking behavior
        when(productRepository.getAllProducts()).thenReturn(Arrays.asList());

        // Act
        ApiResponse<List<ProductDto>> response = productService.getAllProducts();

        // Assert
        assertNotNull(response);
    }
}