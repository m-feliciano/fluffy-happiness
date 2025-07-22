package br.com.microservices.fraud.service;

public interface IFraudCheckService {
    Boolean isFraudster(Long customerId);
}
