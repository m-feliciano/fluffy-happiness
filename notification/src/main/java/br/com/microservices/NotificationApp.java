package br.com.microservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "br.com.microservices.clients")
public class NotificationApp {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApp.class);
    }
}
