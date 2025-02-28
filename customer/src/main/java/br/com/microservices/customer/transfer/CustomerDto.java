package br.com.microservices.customer.transfer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link Customer}
 */
@Builder
public record CustomerDto(Long customerId,
                          @NotNull @Size(min = 2) @NotEmpty String firstname,
                          String lastname,
                          @Email String email) implements Serializable {
}