package br.com.microservices.clients.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for AuthClient - does not add authentication headers
 * to avoid circular dependency during token validation
 */
@Configuration
public class AuthClientConfig {

    @Bean
    public RequestInterceptor authClientRequestInterceptor() {
        return requestTemplate -> {
        };
    }
}

