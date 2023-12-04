package br.com.microservices.notification.rabbitmq;

import br.com.microservices.clients.notification.NotificationRequest;
import br.com.microservices.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(NotificationRequest notificationRequest) {
        log.info("Consuming {} from queue", notificationRequest);
        notificationService.sendNotification(notificationRequest);
    }
}
