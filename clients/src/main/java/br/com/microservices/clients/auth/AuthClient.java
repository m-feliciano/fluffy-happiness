package br.com.microservices.clients.auth;

import br.com.microservices.clients.config.AuthClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "auth-service",
        path = "/api/v1/auth",
        configuration = AuthClientConfig.class
)
public interface AuthClient {

    @GetMapping("/validate")
    Boolean validateToken(@RequestParam("token") String token);
}

