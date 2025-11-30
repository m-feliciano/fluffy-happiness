package br.com.microservices.auth.service;

import br.com.microservices.auth.domain.model.RefreshToken;
import br.com.microservices.auth.domain.model.User;

public interface IRefreshTokenService {

    RefreshToken genearteRefreshToken(User user);

    RefreshToken verifyExpiration(RefreshToken token);

    RefreshToken findByToken(String token);

    void revokeToken(String token);

    void revokeAllUserTokens(User user);
}

