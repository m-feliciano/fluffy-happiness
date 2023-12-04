package br.com.microservices.costumer;

import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class CostumerConfiguration {

    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queues.costumer}")
    private String costumerQueue;

    @Value("${rabbitmq.routing-keys.internal-costumer}")
    private String internalCostumerRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue costumerQueue() {
        return new Queue(this.costumerQueue);
    }

    @Bean
    public Binding internalCostumerBinding() {
        return BindingBuilder
                .bind(costumerQueue())
                .to(internalTopicExchange())
                .with(this.internalCostumerRoutingKey);
    }
}
