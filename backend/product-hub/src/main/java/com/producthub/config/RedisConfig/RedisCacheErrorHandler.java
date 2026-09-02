package com.producthub.config.RedisConfig;

import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(
            RuntimeException exception,
            Cache cache,
            Object key) {

        System.out.println(
                "Redis GET failed for key: " + key +
                ". Continuing without cache."
        );
    }

    @Override
    public void handleCachePutError(
            RuntimeException exception,
            Cache cache,
            Object key,
            Object value) {

        System.out.println(
                "Redis PUT failed for key: " + key +
                ". Continuing without cache."
        );
    }

    @Override
    public void handleCacheEvictError(
            RuntimeException exception,
            Cache cache,
            Object key) {

        System.out.println(
                "Redis EVICT failed for key: " + key +
                ". Continuing without cache."
        );
    }

    @Override
    public void handleCacheClearError(
            RuntimeException exception,
            Cache cache) {

        System.out.println(
                "Redis CLEAR failed. Continuing without cache."
        );
    }
}