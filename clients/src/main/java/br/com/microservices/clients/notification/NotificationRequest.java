package br.com.microservices.clients.notification;

import lombok.Builder;
import lombok.NonNull;

import java.io.Serializable;

/**
 * DTO
 */
@Builder
public record NotificationRequest(@NonNull Long toCustomerId,
                                  @NonNull String toCustomerEmail,
                                  @NonNull String message) implements Serializable {
}