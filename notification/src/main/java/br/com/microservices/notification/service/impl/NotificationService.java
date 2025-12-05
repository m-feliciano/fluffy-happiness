package br.com.microservices.notification.service.impl;

import br.com.microservices.clients.notification.NotificationRequest;
import br.com.microservices.notification.domain.mapper.NotificationMapper;
import br.com.microservices.notification.domain.model.Notification;
import br.com.microservices.notification.domain.response.NotificationResponse;
import br.com.microservices.notification.repository.NotificationRepository;
import br.com.microservices.notification.service.INotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
@Service
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationResponse send(NotificationRequest notificationRequest) {
        log.info("[NOTIFICATION] Sending notification {}", notificationRequest);

        Notification notification = Notification.builder()
                .sender("MyDomain")
                .toCustomerId(notificationRequest.toCustomerId())
                .toCustomerEmail(notificationRequest.toCustomerEmail())
                .message(notificationRequest.message())
                .createdAt(LocalDateTime.now())
                .build();
        Notification saved = notificationRepository.save(notification);
        return notificationMapper.toResponse(saved);
    }

    public Page<NotificationResponse> findAll(Pageable pageRequest) {
        log.info("[NOTIFICATION] Finding all notifications");
        return notificationRepository.findAll(pageRequest).map(notificationMapper::toResponse);
    }
}
