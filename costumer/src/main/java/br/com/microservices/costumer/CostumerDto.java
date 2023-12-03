package br.com.microservices.costumer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;


/**
 * DTO for {@link Costumer}
 */
@Builder
public record CostumerDto(@NotNull @Size(min = 3) String firstname,
                          @NotNull @Size(min = 3) String lastname,
                          @NotNull @Email String email) implements Serializable {
}