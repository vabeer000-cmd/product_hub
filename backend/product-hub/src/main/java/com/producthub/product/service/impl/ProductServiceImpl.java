package com.producthub.product.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.producthub.product.common.exception.ProductNotFoundException;
import com.producthub.product.common.specification.ProductSpecification;
import com.producthub.product.dto.ProductFilter;
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
	
	
//	@Override
//	public Page<ProductResponse> getAllProducts(
//	        ProductFilter filter,
//	        Pageable pageable) {
//
////	    Specification<Product> specification = 
////	            Specification.where(null);
//
////	    if (filter.getCategory() != null
////	            && !filter.getCategory().isBlank()) {
////
////	        specification = specification.and(
////	                ProductSpecification.hasCategory(
////	                        filter.getCategory()
////	                )
////	        );
////	    }
////
////	    if (filter.getAvailable() != null) {
////
////	        specification = specification.and(
////	                ProductSpecification.isAvailable(
////	                        filter.getAvailable()
////	                )
////	        );
////	    }
//	    
//	    Specification<Product> specification = null;
//
//	    if (filter.getCategory() != null
//	            && !filter.getCategory().isBlank()) {
//
//	        specification = ProductSpecification.hasCategory(
//	                filter.getCategory()
//	        );
//	    }
//
//	    if (filter.getAvailable() != null) {
//
//	        Specification<Product> availableSpec =
//	                ProductSpecification.isAvailable(
//	                        filter.getAvailable()
//	                );
//
//	        specification = specification == null
//	                ? availableSpec
//	                : specification.and(availableSpec);
//	    }
//
//	    if (filter.getMinPrice() != null) {
//
//	        Specification<Product> minPriceSpec =
//	                ProductSpecification.hasMinimumPrice(
//	                        filter.getMinPrice()
//	                );
//
//	        specification = specification == null
//	                ? minPriceSpec
//	                : specification.and(minPriceSpec);
//	    }
//	    
//	    if (filter.getMaxPrice() != null) {
//
//	        Specification<Product> maxPriceSpec =
//	                ProductSpecification.hasMaximumPrice(
//	                        filter.getMaxPrice()
//	                );
//
//	        specification = specification == null
//	                ? maxPriceSpec
//	                : specification.and(maxPriceSpec);
//	    }
//	    
//	    if (filter.getSearch() != null
//	            && !filter.getSearch().isBlank()) {
//
//	        Specification<Product> searchSpec =
//	                ProductSpecification.hasSearch(
//	                        filter.getSearch()
//	                );
//
//	        specification = specification == null
//	                ? searchSpec
//	                : specification.and(searchSpec);
//	    }
//	    
//	    Page<Product> products =
//	            productRepository.findAll(
//	                    specification,
//	                    pageable
//	            );
//
//	    return products.map(this::mapToResponse);
//	}
	

	@Override
	public Page<ProductResponse> getAllProducts(
	        ProductFilter filter,
	        Pageable pageable) {

	    Specification<Product> specification =
	            ProductSpecification.build(filter);

	    Page<Product> products =
	            productRepository.findAll(
	                    specification,
	                    pageable
	            );

	    return products.map(this::mapToResponse);
	}

	
	
	@Override
	public ProductResponse createProduct(ProductRequest request) {


	    Product product = mapToEntity(request);

	    Product savedProduct = productRepository.save(product);

	    return mapToResponse(savedProduct);
	}

	@Cacheable(value = "products", key = "'product:' + #id")
	@Override
	public ProductResponse getProductById(Long id) {

	    Product product = productRepository.findById(id)
	            .orElseThrow(() ->
	                    new ProductNotFoundException(
	                            "Product with id " + id + " not found"
	                    )
	            );

	    return mapToResponse(product);
	}
	
	@CachePut(value = "products", key = "'product:' + #id")
	@Override
	public ProductResponse updateProduct(Long id, ProductRequest request) {
		
		Product product = productRepository.findById(id)
				.orElseThrow(()->
						new ProductNotFoundException("Product with id "+id+" not found"));
		
		product.setName(request.getName());
		product.setDescription(request.getDescription());
	    product.setPrice(request.getPrice());
	    product.setCategory(request.getCategory());
	    product.setImageUrl(request.getImageUrl());

	    Product updatedProduct = productRepository.save(product);
	    
	    return mapToResponse(updatedProduct);
		
	}

	@CacheEvict(value = "products", key = "'product:' + #id")
	@Override
	public void deleteProduct(Long id) {
		
		Product product = productRepository.findById(id)
				.orElseThrow(()->
					new ProductNotFoundException("Product with id "+id+" not found")
				);
		
		productRepository.delete(product);
		
	}
	
}
