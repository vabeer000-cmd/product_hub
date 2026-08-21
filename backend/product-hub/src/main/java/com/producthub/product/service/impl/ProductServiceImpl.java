package com.producthub.product.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.producthub.product.dto.ProductRequest;
import com.producthub.product.dto.ProductResponse;
import com.producthub.product.entity.Product;
import com.producthub.product.repository.ProductRepository;
import com.producthub.product.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{


	private final ProductRepository productRepository;
	
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	private ProductResponse mapToResponse(Product product) {
		
		ProductResponse response = new ProductResponse();
		
		response.setId(product.getId());
		response.setName(product.getName());
		response.setDescription(product.getDescription());
		response.setPrice(product.getPrice());
		response.setCategory(product.getCategory());
		response.setAvailable(product.getAvailable());
		response.setCreatedAt(product.getCreatedAt());
		response.setUpdatedAt(product.getUpdatedAt());
		response.setImageUrl(product.getImageUrl());
		
		return response;
	}

	private Product mapToEntity(ProductRequest request) {

	    Product product = new Product();

	    product.setName(request.getName());
	    product.setDescription(request.getDescription());
	    product.setPrice(request.getPrice());
	    product.setCategory(request.getCategory());
	    product.setImageUrl(request.getImageUrl());
	    product.setAvailable(true);

	    return product;
	}
	
	@Override
	public List<ProductResponse> getAllProducts(){
		
		List<Product> products = productRepository.findAll();
		
		
		return products.stream()
				.map(this::mapToResponse)
				.toList();
	}
	
	

	
	
	@Override
	public ProductResponse createProduct(ProductRequest request) {


	    Product product = mapToEntity(request);

	    Product savedProduct = productRepository.save(product);

	    return mapToResponse(savedProduct);
	}

}
