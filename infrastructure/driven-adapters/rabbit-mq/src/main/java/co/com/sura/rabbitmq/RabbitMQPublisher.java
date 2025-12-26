package co.com.sura.rabbitmq;

import co.com.sura.model.events.gateways.EventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import events.DomainEvent;
import co.com.sura.rabbitmq.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RabbitMQPublisher implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> publish(DomainEvent<?> event) {
        return Mono.fromRunnable(() -> {
            try {
                String payload = objectMapper.writeValueAsString(event.getPayload());
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EXCHANGE_NAME,
                        event.getName(),
                        payload
                );
            } catch (Exception e) {
                throw new RuntimeException("Error publishing event", e);
            }
        });
    }
}