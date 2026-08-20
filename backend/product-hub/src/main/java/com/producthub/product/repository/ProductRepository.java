package com.producthub.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.producthub.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
