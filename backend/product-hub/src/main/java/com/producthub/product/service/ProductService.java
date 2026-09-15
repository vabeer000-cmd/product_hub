package com.producthub.product.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.producthub.product.dto.ProductCreateRequest;
import com.producthub.product.dto.ProductFilter;
import com.producthub.product.dto.ProductResponse;
import com.producthub.product.dto.ProductUpdateRequest;

public interface ProductService {
	
	ProductResponse createProduct(ProductCreateRequest request);
	
	Page<ProductResponse> getAllProducts(
	        ProductFilter filter,
	        Pageable pageable);
	public ProductResponse getProductById(Long id);
	
	ProductResponse updateProduct(Long id, ProductUpdateRequest  request);

	void deleteProduct(Long id);
}
