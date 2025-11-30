package br.com.microservices.auth.service;

public interface ITokenBlacklistService {

    void blacklistToken(String token, long expirationTimeMillis);

    boolean isBlacklisted(String token);

    void removeFromBlacklist(String token);
}

