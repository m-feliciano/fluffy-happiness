package br.com.microservices.clients.fraud;

import br.com.microservices.clients.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient("fraud") // This is the name of the service in the Eureka Server

@FeignClient(
        name = "fraud",
        url = "${clients.fraud.url}",
        configuration = FeignClientConfig.class
)
public interface IFraudClient {

    @GetMapping(path = "/api/v1/fraud-check/{customerId}")
    FraudCheckResponse isFraudster(@PathVariable("customerId") Long customerId);
}

