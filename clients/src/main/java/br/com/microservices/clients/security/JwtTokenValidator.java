package br.com.microservices.clients.security;

import br.com.microservices.clients.auth.AuthClient;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    private final AuthClient authClient;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.validation.use-auth-service:true}")
    private boolean useAuthService;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            log.error("Error checking token expiration", e);
            return true;
        }
    }

    public Boolean validateToken(String token) {
        try {
            // First, check basic JWT validity
            extractAllClaims(token);
            if (isTokenExpired(token)) {
                log.debug("Token is expired");
                return false;
            }

            if (useAuthService) {
                try {
                    Boolean isValid = authClient.validateToken(token);
                    if (Boolean.FALSE.equals(isValid)) {
                        log.warn("Token validation failed in auth-service (possibly blacklisted)");
                        return false;
                    }
                } catch (Exception e) {
                    log.error("Error calling auth-service for token validation, falling back to local validation", e);
                }
            }

            return true;
        } catch (Exception e) {
            log.error("Invalid token", e);
            return false;
        }
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

