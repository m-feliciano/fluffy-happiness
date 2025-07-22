package br.com.microservices.fraud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "br.com.microservices.clients")

@PropertySource("classpath:clients-${spring.profiles.active}.properties")
public class FraudApplication {

    public static void main(String[] args) {
        SpringApplication.run(FraudApplication.class);
    }
}