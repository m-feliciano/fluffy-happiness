package br.com.microservices.clients.costumer;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("costumer")
public interface ICostumerClient {}
