package br.com.microservices.clients.customer;

import org.springframework.cloud.openfeign.FeignClient;

//@FeignClient("Customer")
@FeignClient(
        name = "customer",
        url = "${clients.customer.url}"
)
public interface ICustomerClient {
}
