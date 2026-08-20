package com.producthub.product.service;

import java.util.List;

import com.producthub.product.dto.ProductRequest;
import com.producthub.product.dto.ProductResponse;

public interface ProductService {
	
	ProductResponse createProduct(ProductRequest request);
	
	List<ProductResponse> getAllProducts();

}
