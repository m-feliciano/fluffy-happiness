package br.com.microservices.costumer.transfer;

import br.com.microservices.costumer.Costumer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CostumerMapper {

    public static CostumerDto toDto(Costumer costumer) {
        if (costumer == null) {
            return null;
        }

        return CostumerDto.builder()
                .costumerId(costumer.getId())
                .firstname(costumer.getFirstname())
                .lastname(costumer.getLastname())
                .email(costumer.getEmail())
                .build();
    }

    public static Costumer fromDto(CostumerDto costumerDto) {
        if (costumerDto == null) {
            return null;
        }

        return Costumer.builder()
                .id(costumerDto.costumerId())
                .firstname(costumerDto.firstname())
                .lastname(costumerDto.lastname())
                .email(costumerDto.email())
                .build();
    }
}