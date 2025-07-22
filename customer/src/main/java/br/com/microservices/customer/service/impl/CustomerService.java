package br.com.microservices.customer.service.impl;

import br.com.microservices.amqp.RabbitMQMessageProducer;
import br.com.microservices.clients.fraud.FraudCheckResponse;
import br.com.microservices.clients.fraud.IFraudClient;
import br.com.microservices.clients.notification.NotificationRequest;
import br.com.microservices.customer.domain.model.Customer;
import br.com.microservices.customer.repository.CustomerRepository;
import br.com.microservices.customer.service.ICustomerService;
import br.com.microservices.customer.transfer.CustomerDto;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final RabbitMQMessageProducer mqMessageProducer;
    private final IFraudClient fraudClient;

    public void save(CustomerDto dto) {
        if (isEmailTaken(dto.email())) {
            throw new IllegalStateException("E-mail has already been taken");
        }

        Customer customer = Customer.builder()
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .email(dto.email())
                .build();

        customerRepository.saveAndFlush(customer);
        FraudCheckResponse response = fraudClient.isFraudster(customer.getId());
        if (BooleanUtils.toBoolean(response.isFraud())) {
            throw new IllegalStateException("Fraudster detected");
        }

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .toCustomerEmail(customer.getEmail())
                .toCustomerId(customer.getId())
                .message("Hi %s!%n Welcome to...etc".formatted(customer.getFirstname()))
                .build();
        mqMessageProducer.publish(notificationRequest, "internal.exchange", "internal.notification.routing-key");
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public boolean isEmailTaken(String email) {
        return customerRepository.existsByEmail(email);
    }
}
