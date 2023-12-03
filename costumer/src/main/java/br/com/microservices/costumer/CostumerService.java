package br.com.microservices.costumer;

import br.com.microservices.clients.fraud.FraudCheckResponse;
import br.com.microservices.clients.fraud.IFraudClient;
import br.com.microservices.clients.notification.INotificationClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CostumerService {

    private final CostumerRepository costumerRepository;
    private final IFraudClient fraudClient;
    private final INotificationClient notificationClient;

    public CostumerService(CostumerRepository costumerRepository,
                           IFraudClient fraudClient,
                           INotificationClient notificationClient) {
        this.costumerRepository = costumerRepository;
        this.fraudClient = fraudClient;
        this.notificationClient = notificationClient;
    }

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

        // TODO: send notification here
    }

    public List<Costumer> findAll() {
        return costumerRepository.findAll();
    }

    public boolean isEmailTaken(String email) {
        return costumerRepository.existsByEmail(email);
    }
}
