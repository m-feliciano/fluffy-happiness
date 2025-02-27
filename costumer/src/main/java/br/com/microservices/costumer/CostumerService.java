package br.com.microservices.costumer;

import br.com.microservices.amqp.RabbitMQMessageProducer;
import br.com.microservices.clients.fraud.FraudCheckResponse;
import br.com.microservices.clients.fraud.IFraudClient;
import br.com.microservices.clients.notification.NotificationRequest;
import br.com.microservices.costumer.transfer.CostumerDto;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CostumerService {

    private final CostumerRepository costumerRepository;
    private final RabbitMQMessageProducer mqMessageProducer;
    private final IFraudClient fraudClient;

    public void save(CostumerDto dto) {
        if (isEmailTaken(dto.email())) {
            throw new IllegalStateException("E-mail has already been taken");
        }

        Costumer costumer = Costumer.builder()
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .email(dto.email())
                .build();

        costumerRepository.saveAndFlush(costumer);
        FraudCheckResponse response = fraudClient.isFraudster(costumer.getId());
        if (BooleanUtils.toBoolean(response.isFraud())) {
            throw new IllegalStateException("Fraudster detected");
        }

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .toCostumerEmail(costumer.getEmail())
                .toCostumerId(costumer.getId())
                .message("Hi %s!%n Welcome to...etc".formatted(costumer.getFirstname()))
                .build();
        mqMessageProducer.publish(notificationRequest, "internal.exchange", "internal.notification.routing-key");
    }

    public List<Costumer> findAll() {
        return costumerRepository.findAll();
    }

    public boolean isEmailTaken(String email) {
        return costumerRepository.existsByEmail(email);
    }
}
