package com.producthub.product.dto;

import java.util.List;

public class ProductCursorResponse {

    private List<ProductResponse> content;

    private String nextCursor;

    private boolean hasNext;

    public ProductCursorResponse(
            List<ProductResponse> content,
            String nextCursor,
            boolean hasNext) {

        this.content = content;
        this.nextCursor = nextCursor;
        this.hasNext = hasNext;
    }

    public List<ProductResponse> getContent() {
        return content;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public boolean isHasNext() {
        return hasNext;
    }
}