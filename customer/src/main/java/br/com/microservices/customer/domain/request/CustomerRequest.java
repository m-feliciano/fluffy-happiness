package br.com.microservices.customer.domain.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record CustomerRequest(
        @NotBlank @Size(min = 2) @NotEmpty String firstname,
        @NotBlank String lastname,
        @Email String email) implements Serializable {
}