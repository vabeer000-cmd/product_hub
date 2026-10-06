
#!/usr/bin/env bash

set -e

echo "Starting ProductHub in production profile..."

# Spring Profile
export SPRING_PROFILES_ACTIVE=prod

# Database
export DB_URL="jdbc:mysql://localhost:3306/product_hub"
export DB_USERNAME="root"
export DB_PASSWORD="root"

# JWT
export JWT_SECRET="my-super-secret-key-that-is-at-least-32-characters-long"
export JWT_EXPIRATION=3600000

# Redis
export REDIS_HOST=localhost
export REDIS_PORT=6379

# Start application
mvn spring-boot:run