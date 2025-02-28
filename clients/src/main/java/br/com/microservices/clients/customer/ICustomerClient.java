package br.com.microservices.clients.customer;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("Customer")
public interface ICustomerClient {}
