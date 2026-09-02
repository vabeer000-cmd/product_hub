package com.producthub.product.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.producthub.product.dto.ProductFilter;
import com.producthub.product.dto.ProductRequest;
import com.producthub.product.dto.ProductResponse;

public interface ProductService {
	
	ProductResponse createProduct(ProductRequest request);
	
	Page<ProductResponse> getAllProducts(
	        ProductFilter filter,
	        Pageable pageable);
	public ProductResponse getProductById(Long id);
	
	ProductResponse updateProduct(Long id, ProductRequest request);

	void deleteProduct(Long id);
}
