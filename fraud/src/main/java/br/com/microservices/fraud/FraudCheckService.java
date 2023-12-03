package br.com.microservices.fraud;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FraudCheckService {

    private final FraudCheckRepository fraudCheckRepository;

    public FraudCheckService(FraudCheckRepository fraudCheckRepository) {
        this.fraudCheckRepository = fraudCheckRepository;
    }

    public Boolean isFraudster(@NotNull Long costumerId) {
        Boolean fraudster = fraudCheckRepository.existsByCostumerIdAndIsFraudTrue(costumerId);

        FraudCheckHistory entity = FraudCheckHistory.builder()
                .costumerId(costumerId)
                .isFraud(fraudster)
                .createdAt(LocalDateTime.now())
                .build();
        fraudCheckRepository.save(entity);

        return fraudster;
    }
}
