package br.com.microservices.auth.controller.openapi;

import br.com.microservices.auth.domain.request.LoginRequest;
import br.com.microservices.auth.domain.request.LogoutRequest;
import br.com.microservices.auth.domain.request.RefreshTokenRequest;
import br.com.microservices.auth.domain.request.RegisterRequest;
import br.com.microservices.auth.domain.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication", description = "API for user authentication and authorization")
public interface AuthControllerOpenApi {

    @Operation(summary = "User login", description = "Authenticates user and returns JWT token with refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    ResponseEntity<AuthResponse> login(LoginRequest request);

    @Operation(summary = "User registration", description = "Registers a new user and returns JWT token with refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful"),
            @ApiResponse(responseCode = "400", description = "Invalid data or user already exists")
    })
    ResponseEntity<AuthResponse> register(RegisterRequest request);

    @Operation(summary = "Refresh access token", description = "Uses refresh token to generate new access token and refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired refresh token")
    })
    ResponseEntity<AuthResponse> refreshToken(RefreshTokenRequest request);

    @Operation(summary = "Logout user", description = "Revokes the refresh token and blacklists the access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "400", description = "Invalid tokens")
    })
    ResponseEntity<Void> logout(LogoutRequest request);

    @Operation(summary = "Validate token", description = "Validates if a JWT token is valid and not blacklisted")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token validation result returned")
    })
    ResponseEntity<Boolean> validateToken(String token);
}

