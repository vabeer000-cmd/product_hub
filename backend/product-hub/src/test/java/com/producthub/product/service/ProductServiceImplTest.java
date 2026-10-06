package com.producthub.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.producthub.common.exception.ProductNotFoundException;
import com.producthub.product.dto.ProductCreateRequest;
import com.producthub.product.dto.ProductResponse;
import com.producthub.product.dto.ProductUpdateRequest;
import com.producthub.product.entity.Product;
import com.producthub.product.repository.ProductRepository;
import com.producthub.product.service.impl.ProductServiceImpl;

import jakarta.persistence.OptimisticLockException;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getProductById_shouldReturnProduct_whenProductExists() {

        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("Gaming laptop");
        product.setPrice(new BigDecimal("75000"));
        product.setCategory("Electronics");
        product.setAvailable(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setVersion(0L);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        // Act
        ProductResponse response =
                productService.getProductById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Laptop", response.getName());
        assertEquals(new BigDecimal("75000"), response.getPrice());

        verify(productRepository).findById(1L);
    }
    
    @Test
    void getProductById_shouldThrowException_whenProductDoesNotExist() {

        // Arrange
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(1L)
        );

        // Verify
        verify(productRepository).findById(1L);
    }
    
    @Test
    void createProduct_shouldReturnProductResponse_whenProductIsCreated() {

        // Arrange
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("Laptop");
        request.setDescription("Gaming laptop");
        request.setPrice(new BigDecimal("75000"));
        request.setCategory("Electronics");
        request.setImageUrl("laptop.jpg");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Laptop");
        savedProduct.setDescription("Gaming laptop");
        savedProduct.setPrice(new BigDecimal("75000"));
        savedProduct.setCategory("Electronics");
        savedProduct.setImageUrl("laptop.jpg");
        savedProduct.setAvailable(true);
        savedProduct.setCreatedAt(LocalDateTime.now());
        savedProduct.setUpdatedAt(LocalDateTime.now());
        savedProduct.setVersion(0L);

        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

     // Act
        ProductResponse response =
                productService.createProduct(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Laptop", response.getName());
        assertEquals(new BigDecimal("75000"), response.getPrice());

        // Capture the Product passed to repository.save()
        ArgumentCaptor<Product> productCaptor =
                ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(productCaptor.capture());

        Product capturedProduct = productCaptor.getValue();

        assertEquals("Laptop", capturedProduct.getName());
        assertEquals("Gaming laptop", capturedProduct.getDescription());
        assertEquals(new BigDecimal("75000"), capturedProduct.getPrice());
        assertEquals("Electronics", capturedProduct.getCategory());
        assertTrue(capturedProduct.getAvailable());
    }
    
    
    @Test
    void updateProduct_shouldThrowException_whenVersionDoesNotMatch() {

        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(new BigDecimal("75000"));
        product.setCategory("Electronics");
        product.setAvailable(true);
        product.setVersion(2L);

        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setName("Updated Laptop");
        request.setPrice(new BigDecimal("80000"));
        request.setCategory("Electronics");
        request.setVersion(1L); // stale version

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        // Act + Assert
        assertThrows(
                OptimisticLockException.class,
                () -> productService.updateProduct(1L, request)
        );

        // Verify
        verify(productRepository).findById(1L);

        // saveAndFlush() should NOT happen
        verify(productRepository, never()).saveAndFlush(any(Product.class));
    }
    
    
}