package br.com.microservices.notification.service;

import br.com.microservices.clients.notification.NotificationRequest;
import br.com.microservices.notification.domain.model.Notification;

import java.util.List;

public interface INotificationService {

    void send(NotificationRequest notificationRequest);

    List<Notification> findAll();
}
