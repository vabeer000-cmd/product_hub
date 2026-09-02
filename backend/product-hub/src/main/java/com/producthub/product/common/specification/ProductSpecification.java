package com.producthub.product.common.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.producthub.product.dto.ProductFilter;
import com.producthub.product.entity.Product;

public class ProductSpecification {

	public ProductSpecification() {
		// TODO Auto-generated constructor stub
	}
	
	public static Specification<Product> hasCategory(String category){
		
		return (root,query,criteriaBuilder)->
		criteriaBuilder.equal(
				root.get("category"),
				category);
	}
	
	public static Specification<Product> isAvailable(Boolean available) {

	    return (root, query, criteriaBuilder) ->
	            criteriaBuilder.equal(
	                    root.get("available"),
	                    available
	            );
	}
	
	public static Specification<Product> hasMinimumPrice(
	        BigDecimal minPrice) {

	    return (root, query, criteriaBuilder) ->
	            criteriaBuilder.greaterThanOrEqualTo(
	                    root.get("price"),
	                    minPrice
	            );
	}
	
	public static Specification<Product> hasMaximumPrice(
	        BigDecimal maxPrice) {

	    return (root, query, criteriaBuilder) ->
	            criteriaBuilder.lessThanOrEqualTo(
	                    root.get("price"),
	                    maxPrice
	            );
	}
	
	public static Specification<Product> hasSearch(String search) {

	    return (root, query, criteriaBuilder) -> {

	        String pattern = "%" + search.toLowerCase() + "%";

	        return criteriaBuilder.or(
	                criteriaBuilder.like(
	                        criteriaBuilder.lower(root.get("name")),
	                        pattern
	                ),
	                criteriaBuilder.like(
	                        criteriaBuilder.lower(root.get("description")),
	                        pattern
	                )
	        );
	    };
	}
	
	public static Specification<Product> build(ProductFilter filter) {

	    Specification<Product> specification = null;

	    if (filter.getCategory() != null
	            && !filter.getCategory().isBlank()) {

	        specification = ProductSpecification.hasCategory(
	                filter.getCategory()
	        );
	    }

	    if (filter.getAvailable() != null) {

	        Specification<Product> availableSpec =
	                ProductSpecification.isAvailable(
	                        filter.getAvailable()
	                );

	        specification = specification == null
	                ? availableSpec
	                : specification.and(availableSpec);
	    }

	    if (filter.getMinPrice() != null) {

	        Specification<Product> minPriceSpec =
	                ProductSpecification.hasMinimumPrice(
	                        filter.getMinPrice()
	                );

	        specification = specification == null
	                ? minPriceSpec
	                : specification.and(minPriceSpec);
	    }

	    if (filter.getMaxPrice() != null) {

	        Specification<Product> maxPriceSpec =
	                ProductSpecification.hasMaximumPrice(
	                        filter.getMaxPrice()
	                );

	        specification = specification == null
	                ? maxPriceSpec
	                : specification.and(maxPriceSpec);
	    }

	    if (filter.getSearch() != null
	            && !filter.getSearch().isBlank()) {

	        Specification<Product> searchSpec =
	                ProductSpecification.hasSearch(
	                        filter.getSearch()
	                );

	        specification = specification == null
	                ? searchSpec
	                : specification.and(searchSpec);
	    }

	    return specification;
	}
}
