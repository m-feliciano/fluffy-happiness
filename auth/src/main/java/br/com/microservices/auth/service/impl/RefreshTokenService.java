package br.com.microservices.auth.service.impl;

import br.com.microservices.auth.domain.model.RefreshToken;
import br.com.microservices.auth.domain.model.User;
import br.com.microservices.auth.repository.RefreshTokenRepository;
import br.com.microservices.auth.service.IRefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenDurationMs;

    @Override
    @Transactional
    public RefreshToken genearteRefreshToken(User user) {
        refreshTokenRepository.findByUserAndRevokedFalse(user)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                    log.info("Revoked existing refresh token for user: {}", user.getUsername());
                });

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .revoked(false)
                .build();

        refreshToken = refreshTokenRepository.save(refreshToken);
        log.info("Created refresh token for user: {}", user.getUsername());

        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired. Please login again.");
        }

        if (token.getRevoked()) {
            throw new RuntimeException("Refresh token has been revoked. Please login again.");
        }

        return token;
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
    }

    @Override
    @Transactional
    public void revokeToken(String token) {
        RefreshToken refreshToken = findByToken(token);
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
        log.info("Revoked refresh token");
    }

    @Override
    @Transactional
    public void revokeAllUserTokens(User user) {
        refreshTokenRepository.deleteByUser(user);
        log.info("Revoked all refresh tokens for user: {}", user.getUsername());
    }
}

