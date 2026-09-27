package com.producthub.user.service;

import com.producthub.user.dto.LoginRequest;
import com.producthub.user.dto.LoginResponse;
import com.producthub.user.dto.RegisterRequest;

public interface UserService {

	void register(RegisterRequest request);

	LoginResponse login(LoginRequest request);
    
}