package com.producthub.product.service.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.producthub.product.dto.ProductFilter;
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
	
//	   @GetMapping
//	    public Page<ProductResponse> getAllProducts(  
//	    		  @RequestParam(required = false) String category,
//	    		  @RequestParam(required = false) Boolean available,
//	    		  Pageable pageable) {
//		   
//		   if (pageable.getSort().isUnsorted()) {
//		        pageable = PageRequest.of(
//		                pageable.getPageNumber(),
//		                pageable.getPageSize(),
//		                Sort.by(Sort.Direction.DESC, "createdAt")
//		        );
//		    }
//
//	        return productService.getAllProducts(category,available, pageable);
//	    }
//	   
	
	@GetMapping
	public Page<ProductResponse> getAllProducts(
	        @RequestParam(required = false) String category,
	        @RequestParam(required = false) Boolean available,
	        @RequestParam(required = false) BigDecimal minPrice,
	        @RequestParam(required = false) BigDecimal maxPrice,
	        @RequestParam(required = false) String search,
	        Pageable pageable) {

	    ProductFilter filter = new ProductFilter();

	    filter.setCategory(category);
	    filter.setAvailable(available);
	    filter.setMinPrice(minPrice);
	    filter.setMaxPrice(maxPrice);
	    filter.setSearch(search);

	    if (pageable.getSort().isUnsorted()) {
	        pageable = PageRequest.of(
	                pageable.getPageNumber(),
	                pageable.getPageSize(),
	                Sort.by(
	                		   Sort.Order.desc("createdAt"),
	                		    Sort.Order.desc("id")
	                )
	        );
	    }

	    return productService.getAllProducts(
	            filter,
	            pageable
	    );
	}
	   @GetMapping("/{id}")
	   public ProductResponse getProductById(@PathVariable Long id) {

	       return productService.getProductById(id);
	   }
	   
	   @PutMapping("/{id}")
	   public ProductResponse updateProduct(@PathVariable Long id,
			   								@Valid @RequestBody ProductRequest request) {
		   return productService.updateProduct(id, request);
		   
	   }
	   
	   @DeleteMapping("/{id}")
	   @ResponseStatus(HttpStatus.NO_CONTENT)
	   public void deleteProduct(@PathVariable Long id) {
		   
		   productService.deleteProduct(id);
		   
	   }
}
