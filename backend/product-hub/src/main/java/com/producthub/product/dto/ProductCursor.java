package com.producthub.product.dto;

import java.time.LocalDateTime;

public class ProductCursor {

    private LocalDateTime createdAt;
    private Long id;

    public ProductCursor(LocalDateTime createdAt, Long id) {
        this.createdAt = createdAt;
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }
}