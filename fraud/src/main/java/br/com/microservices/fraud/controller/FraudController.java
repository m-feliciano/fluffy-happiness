package br.com.microservices.fraud.controller;

import br.com.microservices.clients.fraud.FraudCheckResponse;
import br.com.microservices.fraud.service.IFraudCheckService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/fraud-check")
public class FraudController {

    private final IFraudCheckService fraudCheckService;

    @GetMapping(path = "/{customerId}")
    public FraudCheckResponse isFraudster(@PathVariable(value = "customerId") Long customerId) {
        log.info("fraud check request for customer {}", customerId);
        boolean isFraudster = fraudCheckService.isFraudster(customerId);
        return new FraudCheckResponse(isFraudster);
    }
}
