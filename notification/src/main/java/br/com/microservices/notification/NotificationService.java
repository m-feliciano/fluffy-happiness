package br.com.microservices.notification;

import br.com.microservices.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void send(NotificationRequest notificationRequest) {
        Notification notification = Notification.builder()
                .sender("MyDomain")
                .toCustomerId(notificationRequest.toCostumerId())
                .toCustomerEmail(notificationRequest.toCostumerEmail())
                .message(notificationRequest.message())
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }
}
