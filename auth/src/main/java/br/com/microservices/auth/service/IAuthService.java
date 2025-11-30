package br.com.microservices.auth.service;

import br.com.microservices.auth.domain.request.LoginRequest;
import br.com.microservices.auth.domain.request.LogoutRequest;
import br.com.microservices.auth.domain.request.RefreshTokenRequest;
import br.com.microservices.auth.domain.request.RegisterRequest;
import br.com.microservices.auth.domain.response.AuthResponse;

public interface IAuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    Boolean validateToken(String token);

    void logout(LogoutRequest request);
}

