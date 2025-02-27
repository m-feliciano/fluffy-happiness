package br.com.microservices.costumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication(
        scanBasePackages = {
                "br.com.microservices.amqp",
                "br.com.microservices.costumer"
        }
)
@EnableFeignClients(basePackages = {
        "br.com.microservices.clients"
})
public class CostumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CostumerApplication.class);
    }
}