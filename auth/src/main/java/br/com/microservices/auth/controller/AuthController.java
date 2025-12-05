package br.com.microservices.auth.controller;

import br.com.microservices.auth.controller.openapi.AuthControllerOpenApi;
import br.com.microservices.auth.domain.request.LoginRequest;
import br.com.microservices.auth.domain.request.LogoutRequest;
import br.com.microservices.auth.domain.request.RefreshTokenRequest;
import br.com.microservices.auth.domain.request.RegisterRequest;
import br.com.microservices.auth.domain.response.AuthResponse;
import br.com.microservices.auth.service.IAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerOpenApi {

    private final IAuthService authService;

    @PostMapping("/login")
    @Override
    public ResponseEntity<AuthResponse> login(@RequestBody @Validated LoginRequest request) {
        log.info("Login request for user: {}", request.getUsername());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Override
    public ResponseEntity<AuthResponse> register(@RequestBody @Validated RegisterRequest request) {
        log.info("Registration request for user: {}", request.getUsername());
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Override
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody @Validated RefreshTokenRequest request) {
        log.info("Refresh token request");
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Override
    public ResponseEntity<Void> logout(@RequestBody @Validated LogoutRequest request) {
        log.info("Logout request");
        authService.logout(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate")
    @Override
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        log.info("Token validation request");
        Boolean isValid = authService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }
}

