package br.com.microservices.notification.domain.mapper;

import br.com.microservices.notification.domain.model.Notification;
import br.com.microservices.notification.domain.response.NotificationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface NotificationMapper {

    NotificationResponse toResponse(Notification notification);

    Notification fromResponse(NotificationResponse notificationResponse);
}

