package br.com.microservices.costumer.transfer;

import br.com.microservices.costumer.Costumer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link Costumer}
 */
@Builder
public record CostumerDto(Long costumerId,
                          @NotNull @Size(min = 2) @NotEmpty String firstname,
                          String lastname,
                          @Email String email) implements Serializable {
}