package com.producthub.product.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.producthub.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

//	@Query("""
//			SELECT p
//			FROM Product p
//			WHERE
//			    p.createdAt < :afterCreatedAt
//			    OR (
//			        p.createdAt = :afterCreatedAt
//			        AND p.id < :afterId
//			    )
//			ORDER BY p.createdAt DESC, p.id DESC
//			""")
//	List<Product> findNextProducts(@Param("afterCreatedAt") LocalDateTime afterCreatedAt,
//			@Param("afterId") Long afterId, Pageable pageable);


	@Query("""
		    SELECT p
		    FROM Product p
		    WHERE
		        (:category IS NULL OR p.category = :category)
		        AND (:available IS NULL OR p.available = :available)
		        AND (:minPrice IS NULL OR p.price >= :minPrice)
		        AND (:maxPrice IS NULL OR p.price <= :maxPrice)
		        AND (
		            :search IS NULL
		            OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
		            OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))
		        )
		        AND (
		            :afterCreatedAt IS NULL
		            OR p.createdAt < :afterCreatedAt
		            OR (
		                p.createdAt = :afterCreatedAt
		                AND p.id < :afterId
		            )
		        )
		    ORDER BY p.createdAt DESC, p.id DESC
		    """)
		List<Product> findProductsByCursor(
		        @Param("category") String category,
		        @Param("available") Boolean available,
		        @Param("minPrice") BigDecimal minPrice,
		        @Param("maxPrice") BigDecimal maxPrice,
		        @Param("search") String search,
		        @Param("afterCreatedAt") LocalDateTime afterCreatedAt,
		        @Param("afterId") Long afterId,
		        Pageable pageable
		);
}
