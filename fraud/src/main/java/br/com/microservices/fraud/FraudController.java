package br.com.microservices.fraud;

import br.com.microservices.clients.fraud.FraudCheckResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/fraud-check")
public class FraudController {

    private final FraudCheckService fraudCheckService;

    public FraudController(FraudCheckService fraudCheckService) {
        this.fraudCheckService = fraudCheckService;
    }

    @GetMapping(path = "{costumerId}")
    public FraudCheckResponse isFraudster(@PathVariable(value = "costumerId") Long customerId) {
        log.info("fraud check request for costumer {}", customerId);
        boolean isFraudster = fraudCheckService.isFraudster(customerId);
        return new FraudCheckResponse(isFraudster);
    }
}
