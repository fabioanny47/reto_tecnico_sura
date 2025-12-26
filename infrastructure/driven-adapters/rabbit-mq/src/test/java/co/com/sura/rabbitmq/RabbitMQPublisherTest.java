package co.com.sura.rabbitmq;

import co.com.sura.model.policy.Policy;
import co.com.sura.rabbitmq.config.RabbitMQConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import events.DomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.test.StepVerifier;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitMQPublisherTest {

    RabbitMQPublisher  rabbitMQPublisher;
    @Mock
    RabbitTemplate rabbitTemplate;
    @Mock
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        rabbitMQPublisher = new RabbitMQPublisher( rabbitTemplate, objectMapper);
    }

    @Test
    void mustPublishSuccessfully() throws JsonProcessingException {
        Policy payload = Policy.builder().policyId("test").build();
        DomainEvent<Policy> event = new DomainEvent<>("policy.created", UUID.randomUUID(), payload);

        when(objectMapper.writeValueAsString(payload)).thenReturn("{\"policyId\":\"test\"}");

        StepVerifier.create(rabbitMQPublisher.publish(event))
                .verifyComplete();

        verify(rabbitTemplate).convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                event.getName(),
                "{\"policyId\":\"test\"}"
        );
    }

    @Test
    void mustPropagateSerializationError() throws JsonProcessingException {
        Policy payload = Policy.builder().policyId("test").build();
        DomainEvent<Policy> event = new DomainEvent<>("policy.created", UUID.randomUUID(), payload);

        when(objectMapper.writeValueAsString(payload)).thenThrow(new JsonProcessingException("fail") {});

        StepVerifier.create(rabbitMQPublisher.publish(event))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().contains("Error publishing event")
                )
                .verify();

        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), anyString());
    }
}