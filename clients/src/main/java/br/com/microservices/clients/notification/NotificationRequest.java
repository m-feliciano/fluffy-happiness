package br.com.microservices.clients.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.NonNull;

import java.io.Serializable;

/**
 * DTO
 */
@Builder
public record NotificationRequest(@NonNull Long toCustomerId,
                                  @NotBlank String toCustomerEmail,
                                  @NotBlank String message) implements Serializable {
}