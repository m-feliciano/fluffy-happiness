package br.com.microservices.fraud;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class FraudCheckService {

    private final FraudCheckRepository fraudCheckRepository;

    public Boolean isFraudster(@NotNull Long customerId) {
        Boolean fraudster = fraudCheckRepository.existsBycustomerIdAndIsFraudTrue(customerId);

        FraudCheckHistory entity = FraudCheckHistory.builder()
                .customerId(customerId)
                .isFraud(fraudster)
                .createdAt(LocalDateTime.now())
                .build();
        fraudCheckRepository.save(entity);

        return fraudster;
    }
}
