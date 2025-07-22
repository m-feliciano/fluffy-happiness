package br.com.microservices.notification.service.impl;

import br.com.microservices.clients.notification.NotificationRequest;
import br.com.microservices.notification.domain.model.Notification;
import br.com.microservices.notification.repository.NotificationRepository;
import br.com.microservices.notification.service.INotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;

    public void send(NotificationRequest notificationRequest) {
        Notification notification = Notification.builder()
                .sender("MyDomain")
                .toCustomerId(notificationRequest.toCustomerId())
                .toCustomerEmail(notificationRequest.toCustomerEmail())
                .message(notificationRequest.message())
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }

    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }
}
