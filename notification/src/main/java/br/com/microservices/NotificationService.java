package br.com.microservices;

import br.com.microservices.clients.notification.NotificationRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    void sendNotification(NotificationRequest request) {
        Notification notification = Notification.builder()
                .fromSenderId(request.fromSenderId())
                .fromSenderEmail(request.fromSenderEmail())
                .toCustomerId(request.toCostumerId())
                .toCustomerEmail(request.toCostumerEmail())
                .message(request.message())
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);

        // TODO async send i.e queue service
    }
}
