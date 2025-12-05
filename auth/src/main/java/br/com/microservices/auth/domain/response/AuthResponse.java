package br.com.microservices.auth.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;
    private Long expiresIn;
    private String refreshToken;
    private Long refreshExpiresIn;
    private String type = "Bearer";
    private String username;
    private String email;
    private Set<String> roles;
}

