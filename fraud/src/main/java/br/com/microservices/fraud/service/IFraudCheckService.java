package br.com.microservices.fraud.service;

public interface IFraudCheckService {
    boolean isFraudster(Long customerId);
}
