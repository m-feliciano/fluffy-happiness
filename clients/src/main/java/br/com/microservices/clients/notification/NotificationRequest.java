package br.com.microservices.clients.notification;

import lombok.Builder;
import lombok.NonNull;

import java.io.Serializable;

/**
 * DTO
 */
@Builder
public record NotificationRequest(@NonNull Integer toCostumerId,
                                  String toCostumerEmail,
                                  @NonNull Integer fromSenderId,
                                  String fromSenderEmail,
                                  @NonNull String message) implements Serializable {
}