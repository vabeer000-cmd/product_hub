package com.producthub.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.producthub.config.security.JwtService;
import com.producthub.user.dto.LoginRequest;
import com.producthub.user.dto.LoginResponse;
import com.producthub.user.dto.RefreshTokenRequest;
import com.producthub.user.dto.RegisterRequest;
import com.producthub.user.entity.RefreshToken;
import com.producthub.user.service.RefreshTokenService;
import com.producthub.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(
    		UserService userService,
    		RefreshTokenService refreshTokenService,
    		JwtService jwtService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request) {

        userService.register(request);
    }
    
    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        return userService.login(request);
    }
    
    @PostMapping("/refresh")
    public LoginResponse refreshToken(
           @Valid @RequestBody RefreshTokenRequest request) {

        RefreshToken refreshToken =
                refreshTokenService.findByToken(request.getRefreshToken());

        refreshTokenService.verifyExpiration(refreshToken);

        String accessToken =
                jwtService.generateToken(
                        refreshToken.getUser().getUsername());

        return new LoginResponse(
                accessToken,
                refreshToken.getToken());
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody RefreshTokenRequest request) {

        refreshTokenService.deleteByToken(
                request.getRefreshToken());

        return ResponseEntity.noContent().build();
    }
}