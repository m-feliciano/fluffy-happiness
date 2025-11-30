package br.com.microservices.customer.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link br.com.microservices.customer.domain.model.Customer}
 */
@Builder
@Schema(description = "Response DTO for Customer")
public record CustomerResponse(
        @Schema(description = "ID único do cliente", example = "1")
        Long id,

        @Schema(description = "Primeiro nome do cliente", example = "João")
        String firstname,

        @Schema(description = "Sobrenome do cliente", example = "Silva")
        String lastname,

        @Schema(description = "Email do cliente", example = "joao.silva@example.com")
        String email) implements Serializable {
}