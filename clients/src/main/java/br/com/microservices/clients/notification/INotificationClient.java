package br.com.microservices.clients.notification;

import br.com.microservices.clients.config.feign.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "notification",
        url = "${clients.notification.url}",
        configuration = {FeignClientConfig.class}
)
public interface INotificationClient {

    @PostMapping(path = "/api/v1/")
    void send(@RequestBody NotificationRequest request);
}
