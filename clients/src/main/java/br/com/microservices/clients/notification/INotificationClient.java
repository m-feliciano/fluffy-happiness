package br.com.microservices.clients.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// @FeignClient("notification")
@FeignClient(
        name = "notification",
        url = "${clients.notification.url}"
)
public interface INotificationClient {

    @PostMapping(path = "/api/v1/")
    void send(@RequestBody NotificationRequest request);
}
