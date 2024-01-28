package com.bookstore.applicaton.service;

import com.bookstore.applicaton.common.ApiResponse;
import com.bookstore.applicaton.constants.ProductResponseMessages;
import com.bookstore.applicaton.domain.Product;
import com.bookstore.applicaton.dto.ProductDto;
import com.bookstore.applicaton.mapper.ProductMapper;
import com.bookstore.applicaton.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProductService {

    // In-memory map of products (using product ID as the key)
    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    // Counter for generating unique product IDs
    private AtomicInteger nextProductId = new AtomicInteger(1); // For unique ID generation

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Creates a new product.
     *
     * @param productDto The product to create.
     * @return An ApiResponse containing the created product's information and HTTP status code.
     */
    public ApiResponse<ProductDto> createProduct(ProductDto productDto) {
        try {
            int productId = generateProductId();
            Product createdProduct = new Product(
                    productId,
                    productDto.getName(),
                    productDto.getDescription(),
                    productDto.getPrice(),
                    productDto.getQuantityAvailable()
            );
            Product savedProduct = productRepository.addProduct(createdProduct);
            ProductDto saveddProductDto = productMapper.toDto(savedProduct);
            return new ApiResponse<>(HttpStatus.CREATED.value(), true,
                    ProductResponseMessages.PRODUCT_CREATED_SUCCESSFULLY, saveddProductDto);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), false, e.getMessage(), null);
        }
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId The ID of the product to retrieve.
     * @return An ApiResponse containing the product's information (if found) and HTTP status code.
     */
    public ApiResponse<ProductDto> getProduct(Integer productId) {
        Product product = productRepository.getProduct(productId);
        if (product != null) {
            ProductDto productDto = productMapper.toDto(product);
            return new ApiResponse<>(HttpStatus.OK.value(), true,
                    ProductResponseMessages.PRODUCT_RETRIEVED_SUCCESSFULLY, productDto);
        } else {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), false,
                    ProductResponseMessages.PRODUCT_NOT_FOUND, null);
        }
    }

    /**
     * Updates an existing product.
     *
     * @param productId The ID of the product to update.
     * @param productDto The product with updated information.
     * @return An ApiResponse indicating success or failure.
     */
    public ApiResponse<ProductDto> updateProduct(Integer productId, ProductDto productDto) {
        Product existingProduct = productRepository.getProduct(productId);
        if (existingProduct != null) {
            Product updatedProduct = productMapper.toEntity(productDto);
            updatedProduct.setProductId(productId);
            if (productRepository.updateProduct(productId, updatedProduct)) {
                ProductDto updatedProductDto = productMapper.toDto(updatedProduct);
                return new ApiResponse<>(HttpStatus.OK.value(),
                        true, ProductResponseMessages.PRODUCT_UPDATED_SUCCESSFULLY, updatedProductDto);
            } else {
                return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        false, ProductResponseMessages.FAILED_TO_UPDATE_PRODUCT, null);
            }
        } else {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), false,
                    ProductResponseMessages.PRODUCT_NOT_FOUND, null);
        }
    }


    public ApiResponse<ProductDto> deleteProduct(Integer productId) {
        Product deletedProduct = productRepository.getProduct(productId);
        if (deletedProduct != null && productRepository.deleteProduct(productId)) {
            ProductDto deletedProductDto = productMapper.toDto(deletedProduct);
            return new ApiResponse<>(HttpStatus.OK.value(), true,
                    ProductResponseMessages.PRODUCT_DELETED_SUCCESSFULLY, deletedProductDto);
        } else {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), false,
                    ProductResponseMessages.PRODUCT_NOT_FOUND, null);
        }
    }


    /**
     * Applies a discount or tax to a product's price.
     *
     * @param productId The ID of the product to modify.
     * @param type The type of operation to apply, either "discount" or "tax".
     * @param value The value of the discount or tax to apply.
     * @return An ApiResponse indicating success or failure, along with the updated product information.
     */
    public ApiResponse<ProductDto> applyDiscountOrTax(Integer productId, String type, BigDecimal value) {
        Product product = productRepository.getProduct(productId);
        if (product != null) {
            try {
                if ("discount".equalsIgnoreCase(type)) {
                    product.setPrice(product.applyDiscount(value));
                } else if ("tax".equalsIgnoreCase(type)) {
                    product.setPrice(product.applyTax(value));
                } else {
                    throw new IllegalArgumentException(ProductResponseMessages.INVALID_OPERATION_TYPE);
                }
                if (productRepository.updateProduct(productId, product)) {
                    ProductDto updatedProductDto = productMapper.toDto(product);
                    return new ApiResponse<>(HttpStatus.OK.value(), true,
                            ProductResponseMessages.OPERATION_APPLIED_SUCCESSFULLY, updatedProductDto);
                } else {
                    return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), false,
                            ProductResponseMessages.FAILED_TO_UPDATE_PRODUCT, null);
                }
            } catch (Exception e) {
                return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), false, e.getMessage(), null);
            }
        } else {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), false,
                    ProductResponseMessages.PRODUCT_NOT_FOUND, null);
        }
    }

    /**
     * Retrieves all products.
     *
     * @return An ApiResponse containing the list of products and HTTP status code.
     */
    public ApiResponse<List<ProductDto>> getAllProducts() {
        List<Product> allProducts = productRepository.getAllProducts();
        List<ProductDto> allProductsDto = allProducts.stream()
                .map(productMapper::toDto)
                .toList();
        return new ApiResponse<>(HttpStatus.OK.value(), true,
                ProductResponseMessages.ALL_PRODUCTS_RETRIEVED_SUCCESSFULLY, allProductsDto);
    }

    /**
     * Generates a unique product ID.
     *
     * @return A unique integer value for use as a product ID.
     */
    private int generateProductId() {
        return nextProductId.getAndIncrement();
    }

}