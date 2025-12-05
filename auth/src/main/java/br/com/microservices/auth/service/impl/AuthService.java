package br.com.microservices.auth.service.impl;

import br.com.microservices.auth.domain.model.RefreshToken;
import br.com.microservices.auth.domain.model.Role;
import br.com.microservices.auth.domain.model.RoleName;
import br.com.microservices.auth.domain.model.User;
import br.com.microservices.auth.domain.request.LoginRequest;
import br.com.microservices.auth.domain.request.LogoutRequest;
import br.com.microservices.auth.domain.request.RefreshTokenRequest;
import br.com.microservices.auth.domain.request.RegisterRequest;
import br.com.microservices.auth.domain.response.AuthResponse;
import br.com.microservices.auth.repository.RoleRepository;
import br.com.microservices.auth.repository.UserRepository;
import br.com.microservices.auth.security.CustomUserDetailsService;
import br.com.microservices.auth.security.JwtService;
import br.com.microservices.auth.service.IAuthService;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    public static final String BEARER_PREFIX = "Bearer ";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("User {} attempting to login", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User principal = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(principal);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefreshToken refreshToken = refreshTokenService.genearteRefreshToken(user);
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        log.info("User {} logged in successfully", request.getUsername());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken.getToken())
                .type("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .expiresIn(jwtService.getExpirationTime())
                .refreshExpiresIn(refreshToken.getExpiryDate().toEpochMilli() - System.currentTimeMillis())
                .build();
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .roles(new HashSet<>())
                .build();

        Set<Role> roles = new HashSet<>();
        if (!Collections.isEmpty(request.getRoles())) {
            request.getRoles().forEach(roleName -> {
                Role role = roleRepository.findByName(RoleName.valueOf(roleName))
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                roles.add(role);
            });
        } else {
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Default role not found"));
            roles.add(userRole);
        }

        user.setRoles(roles);
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.genearteRefreshToken(user);

        Set<String> roleNames = roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        log.info("User {} registered successfully", request.getUsername());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken.getToken())
                .type("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roleNames)
                .expiresIn(jwtService.getExpirationTime())
                .refreshExpiresIn(refreshToken.getExpiryDate().toEpochMilli() - System.currentTimeMillis())
                .build();
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        log.info("Refresh token request received");

        var refreshToken = refreshTokenService.findByToken(request.getRefreshToken());
        refreshToken = refreshTokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String newAccessToken = jwtService.generateToken(userDetails);
        RefreshToken newRefreshToken = refreshTokenService.genearteRefreshToken(user);

        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        log.info("Tokens refreshed successfully for user: {}", user.getUsername());

        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .type("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .expiresIn(jwtService.getExpirationTime())
                .refreshExpiresIn(newRefreshToken.getExpiryDate().toEpochMilli() - System.currentTimeMillis())
                .build();
    }

    @Override
    public Boolean validateToken(String token) {
        try {
            if (tokenBlacklistService.isBlacklisted(token)) {
                log.warn("Token is blacklisted");
                return false;
            }

            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtService.validateToken(token, userDetails);
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return false;
        }
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) {
        log.info("Logout request received");

        refreshTokenService.revokeToken(request.getRefreshToken());

        try {
            String accessToken = request.getAccessToken();
            if (accessToken.startsWith(BEARER_PREFIX)) {
                accessToken = accessToken.substring(BEARER_PREFIX.length());
            }

            Date expiration = jwtService.extractExpiration(accessToken);
            long currentTime = System.currentTimeMillis();
            long expirationTime = expiration.getTime();
            long ttl = expirationTime - currentTime;

            if (ttl > 0) {
                tokenBlacklistService.blacklistToken(accessToken, ttl);
                log.info("Access token added to blacklist with TTL: {} ms", ttl);
            } else {
                log.info("Token already expired, no need to blacklist");
            }
        } catch (Exception e) {
            log.error("Error blacklisting access token", e);
        }

        log.info("User logged out successfully");
    }
}

