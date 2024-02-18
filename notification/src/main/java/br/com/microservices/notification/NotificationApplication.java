package br.com.microservices.notification;

import br.com.microservices.amqp.RabbitMQMessageProducer;
import br.com.microservices.notification.amqp.NotificationConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication(
        scanBasePackages = {
                "br.com.microservices.amqp",
                "br.com.microservices.notification",
        }
)
@EnableFeignClients(basePackages = "br.com.microservices.clients")
public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class);
    }

//    public CommandLineRunner commandLineRunner(
//            RabbitMQMessageProducer producer,
//            NotificationConfiguration configuration) {
//        return args -> {
//            producer.publish("Hello there.",
//                    configuration.getInternalExchange(),
//                    configuration.getInternalNotificationRoutingKey());
//        };
//    }
}
