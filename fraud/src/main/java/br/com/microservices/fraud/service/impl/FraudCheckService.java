package br.com.microservices.fraud.service.impl;

import br.com.microservices.fraud.domain.model.FraudCheckHistory;
import br.com.microservices.fraud.repository.FraudCheckRepository;
import br.com.microservices.fraud.service.IFraudCheckService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class FraudCheckService implements IFraudCheckService {

    private final FraudCheckRepository fraudCheckRepository;

    public boolean isFraudster(@NotNull Long customerId) {
        boolean fraudster = fraudCheckRepository.existsByCustomerIdAndIsFraudTrue(customerId);

        FraudCheckHistory entity = FraudCheckHistory.builder()
                .customerId(customerId)
                .isFraud(fraudster)
                .createdAt(LocalDateTime.now())
                .build();
        fraudCheckRepository.save(entity);

        return fraudster;
    }
}
