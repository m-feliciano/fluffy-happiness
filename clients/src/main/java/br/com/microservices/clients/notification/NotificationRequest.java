package br.com.microservices.clients.notification;

import lombok.Builder;
import lombok.NonNull;

import java.io.Serializable;

/**
 * DTO
 */
@Builder
public record NotificationRequest(@NonNull Long tocustomerId,
                                  @NonNull String tocustomerEmail,
                                  @NonNull String message) implements Serializable {
}