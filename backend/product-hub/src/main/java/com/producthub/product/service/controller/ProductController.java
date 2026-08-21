package com.producthub.product.service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.producthub.product.dto.ProductRequest;
import com.producthub.product.dto.ProductResponse;
import com.producthub.product.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	
	public ProductResponse createProduct(@Valid @RequestBody ProductRequest request) {
		
		return productService.createProduct(request);
		
	}
	
	   @GetMapping
	    public List<ProductResponse> getAllProducts() {

	        return productService.getAllProducts();
	    }
}
