package com.producthub.user.service;

import com.producthub.user.entity.RefreshToken;
import com.producthub.user.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyExpiration(RefreshToken refreshToken);

    RefreshToken findByToken(String token);
    
    void deleteByToken(String token);
}