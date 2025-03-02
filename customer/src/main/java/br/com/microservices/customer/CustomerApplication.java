package br.com.microservices.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@EnableDiscoveryClient
@SpringBootApplication(
        scanBasePackages = {
                "br.com.microservices.amqp",
                "br.com.microservices.customer"
        }
)
@EnableFeignClients(basePackages = {
        "br.com.microservices.clients"
})

@PropertySources({
        @PropertySource("classpath:clients-${spring.profiles.active}.properties")
})
public class CustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class);
    }
}