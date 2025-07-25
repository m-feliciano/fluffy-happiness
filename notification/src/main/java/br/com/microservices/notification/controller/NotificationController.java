package br.com.microservices.notification.controller;

import br.com.microservices.clients.notification.NotificationRequest;
import br.com.microservices.notification.domain.model.Notification;
import br.com.microservices.notification.service.INotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final INotificationService notificationService;

    @PostMapping
    void send(@RequestBody NotificationRequest notificationRequest) {
        log.info("Sending notifification {}", notificationRequest);
        this.notificationService.send(notificationRequest);
    }

    @GetMapping
    List<Notification> findAll() {
        return notificationService.findAll();
    }
}
