package com.producthub.product.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.producthub.config.security.JwtService;
import com.producthub.product.dto.ProductResponse;
import com.producthub.product.service.ProductService;
import com.producthub.user.service.impl.CustomUserDetailsService;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;
    
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @MockitoBean
    private CacheManager cacheManager;
    @Test
    void getProductById_shouldReturnProduct() throws Exception {

        // Arrange
        ProductResponse response = new ProductResponse();
        response.setId(1L);
        response.setName("Laptop");
        response.setPrice(new BigDecimal("75000"));

        when(productService.getProductById(1L))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(75000));
    }
    
    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getProductById_shouldReturnProduct_user() throws Exception {

        ProductResponse response = new ProductResponse();
        response.setId(1L);
        response.setName("Laptop");
        response.setPrice(new BigDecimal("75000"));

        when(productService.getProductById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(75000));
    }
    
    
    
}