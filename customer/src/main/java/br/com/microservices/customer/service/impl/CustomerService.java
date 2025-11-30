package br.com.microservices.customer.service.impl;

import br.com.microservices.amqp.RabbitMQMessageProducer;
import br.com.microservices.clients.fraud.FraudCheckResponse;
import br.com.microservices.clients.fraud.IFraudClient;
import br.com.microservices.clients.notification.NotificationRequest;
import br.com.microservices.customer.domain.mapper.CustomerMapper;
import br.com.microservices.customer.domain.model.Customer;
import br.com.microservices.customer.domain.request.CustomerRequest;
import br.com.microservices.customer.domain.response.CustomerResponse;
import br.com.microservices.customer.repository.CustomerRepository;
import br.com.microservices.customer.service.ICustomerService;
import feign.RetryableException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class CustomerService implements ICustomerService {

    public static final String INTERNAL_NOTIFICATION_ROUTING_KEY = "internal.notification.routing-key";
    public static final String INTERNAL_EXCHANGE = "internal.exchange";

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final RabbitMQMessageProducer mqMessageProducer;
    private final IFraudClient fraudClient;

    @Retryable(
            retryFor = {RetryableException.class},
            maxAttemptsExpression = "${fraud.client.retry.max-attempts:5}",
            backoff = @Backoff(
                    delayExpression = "${fraud.client.retry.delay:200}",
                    maxDelayExpression = "${fraud.client.retry.max-delay:3000}",
                    multiplierExpression = "${fraud.client.retry.multiplier:1.5}")
    )
    public CustomerResponse save(CustomerRequest request) {
        log.info("[CUSTOMER] Saving customer {}", request);
        if (isEmailTaken(request.email())) {
            log.warn("[CUSTOMER] E-mail has already been taken");
            throw new IllegalStateException("E-mail has already been taken");
        }

        final Customer customer = customerMapper.toCustomer(request);

        log.info("[CUSTOMER] Saving customer {}", customer);
        customerRepository.saveAndFlush(customer);

        log.info("[CUSTOMER] Sending welcome message to customer {}", customer);
        FraudCheckResponse response = fraudClient.isFraudster(customer.getId());
        if (BooleanUtils.toBoolean(response.isFraud())) {
            log.warn("[CUSTOMER] Fraudster detected {}", customer);
            throw new IllegalStateException("Fraudster detected");
        }

        log.info("[CUSTOMER] Publishing notification for customer {}", customer);

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .toCustomerEmail(customer.getEmail())
                .toCustomerId(customer.getId())
                .message("Hi " + customer.getFirstname() + ", welcome to our service!")
                .build();
        mqMessageProducer.publish(notificationRequest, INTERNAL_EXCHANGE, INTERNAL_NOTIFICATION_ROUTING_KEY);
        return customerMapper.toResponse(customer);
    }

    public Page<CustomerResponse> findAll(Pageable pageable) {
        log.info("[CUSTOMER] Finding all customers");
        return customerRepository.findAll(pageable).map(customerMapper::toResponse);
    }

    public boolean isEmailTaken(String email) {
        log.info("[CUSTOMER] Checking if email is taken: {}", email);
        return customerRepository.existsByEmail(email);
    }

    @Recover
    public void recover(RetryableException e, CustomerResponse customerResponse) {
        log.error("[CUSTOMER] All retry attempts to call Fraud Service have been exhausted. Fallback method invoked.", e);
        throw new IllegalStateException("Fraud Service is currently unavailable. Please try again later.");
    }
}
