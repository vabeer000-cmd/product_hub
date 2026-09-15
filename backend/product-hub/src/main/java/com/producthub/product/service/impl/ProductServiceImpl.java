package com.producthub.product.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.producthub.product.common.exception.ProductNotFoundException;
import com.producthub.product.common.specification.ProductSpecification;
import com.producthub.product.dto.ProductCreateRequest;
import com.producthub.product.dto.ProductFilter;
import com.producthub.product.dto.ProductResponse;
import com.producthub.product.dto.ProductUpdateRequest;
import com.producthub.product.entity.Product;
import com.producthub.product.repository.ProductRepository;
import com.producthub.product.service.ProductService;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService{


	private final ProductRepository productRepository;
	private static final Logger log =
	        LoggerFactory.getLogger(ProductServiceImpl.class);
	
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
		response.setVersion(product.getVersion());
		
		return response;
	}

	private Product mapToEntity(ProductCreateRequest request) {

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
        System.err.println("products list length : "+products.getSize());
	    return products.map(this::mapToResponse);
	}

	
	
	@Override
	public ProductResponse createProduct(ProductCreateRequest request) {
		log.info("Creating product with name: {}", request.getName());

	    Product product = mapToEntity(request);

	    Product savedProduct = productRepository.save(product);

	    log.info(
	            "Product created successfully. id: {}, version: {}",
	            savedProduct.getId(),
	            savedProduct.getVersion()
	    );
	    return mapToResponse(savedProduct);
	}

	@Cacheable(value = "products", key = "'product:' + #id")
	@Override
	public ProductResponse getProductById(Long id) {
		 log.info("Getting product. id: {}", id);
	    Product product = productRepository.findById(id)
	            .orElseThrow(() ->
	                    new ProductNotFoundException(
	                            "Product with id " + id + " not found"
	                    )
	            );

	    return mapToResponse(product);
	}
	
	
	@Transactional
	@CachePut(value = "products", key = "'product:' + #id")
	@Override
	public ProductResponse updateProduct(Long id, ProductUpdateRequest  request) {
		log.info("Updating product. id: {}", id);
		Product product = productRepository.findById(id)
				.orElseThrow(()->
						new ProductNotFoundException("Product with id "+id+" not found"));
		
		if (!request.getVersion().equals(product.getVersion())) {
			  log.warn(
			            "Optimistic lock conflict. id: {}, request version: {}, current version: {}",
			            id,
			            request.getVersion(),
			            product.getVersion()
			    );
		    throw new OptimisticLockException(
		            "Product was already modified by another user"
		    );
		}
		product.setName(request.getName());
		product.setDescription(request.getDescription());
	    product.setPrice(request.getPrice());
	    product.setCategory(request.getCategory());
	    product.setImageUrl(request.getImageUrl());

//	    Product updatedProduct = productRepository.save(product);
	    Product updatedProduct = productRepository.saveAndFlush(product);
	    log.info(
	            "Product updated successfully. id: {}, new version: {}",
	            id,
	            updatedProduct.getVersion()
	    );
	    
	    return mapToResponse(updatedProduct);
		
	}
	
	

	@CacheEvict(value = "products", key = "'product:' + #id")
	@Override
	public void deleteProduct(Long id) {
		log.info("Deleting product. id: {}", id);
		Product product = productRepository.findById(id)
				.orElseThrow(()->
					new ProductNotFoundException("Product with id "+id+" not found")
				);
		
		productRepository.delete(product);
		log.info("Product deleted successfully. id: {}", id);
		
	}
	
}
