package com.producthub.user.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.producthub.common.exception.InvalidRefreshTokenException;
import com.producthub.user.entity.RefreshToken;
import com.producthub.user.entity.User;
import com.producthub.user.repository.RefreshTokenRepository;
import com.producthub.user.service.RefreshTokenService;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(
            RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken createRefreshToken(User user) {

        refreshTokenRepository.deleteByUserId(user.getId());

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(
                LocalDateTime.now().plusDays(7));

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new InvalidRefreshTokenException("Refresh token not found"));
    }

    @Override
    @Transactional
    public RefreshToken verifyExpiration(
            RefreshToken refreshToken) {

        if (refreshToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            refreshTokenRepository.delete(refreshToken);

            throw new InvalidRefreshTokenException("Refresh token expired");
        }

        return refreshToken;
    }
    
    @Override
    @Transactional
    public void deleteByToken(String token) {
        RefreshToken refreshToken = findByToken(token);
        refreshTokenRepository.delete(refreshToken);
    }
}