package br.com.microservices.notification.controller;

import br.com.microservices.clients.notification.NotificationRequest;
import br.com.microservices.notification.controller.openapi.NotificationControllerOpenApi;
import br.com.microservices.notification.domain.response.NotificationResponse;
import br.com.microservices.notification.service.INotificationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController implements NotificationControllerOpenApi {

    private final INotificationService notificationService;

    @PostMapping
    @Override
    public ResponseEntity<NotificationResponse> send(@RequestBody @Valid NotificationRequest notificationRequest,
                                                     @RequestHeader(name = "Authorization", required = false) String authorization) {
        log.info("Sending notification: {}", notificationRequest);
        NotificationResponse response = this.notificationService.send(notificationRequest);
        log.info("Notification sent successfully: {}", response);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        log.info("Notification location: {}", location);
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Override
    public Page<NotificationResponse> findAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageRequest,
            @RequestHeader(name = "Authorization", required = false) String authorization) {
        log.info("Finding all notifications");
        return notificationService.findAll(pageRequest);
    }
}
