package br.com.microservices.customer.amqp;

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
public class CustomerConfiguration {

    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queues.customer}")
    private String customerQueue;

    @Value("${rabbitmq.routing-keys.internal-customer}")
    private String internalcustomerRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue customerQueue() {
        return new Queue(this.customerQueue);
    }

    @Bean
    public Binding internalcustomerBinding() {
        return BindingBuilder
                .bind(customerQueue())
                .to(internalTopicExchange())
                .with(this.internalcustomerRoutingKey);
    }
}
