package br.com.microservices.notification.service;

import br.com.microservices.clients.notification.NotificationRequest;
import br.com.microservices.notification.domain.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface INotificationService {

    NotificationResponse send(NotificationRequest notificationRequest);

    Page<NotificationResponse> findAll(Pageable pageRequest);
}
