package br.com.microservices.clients.fraud;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record FraudCheckResponse(Boolean isFraud) implements Serializable {
}