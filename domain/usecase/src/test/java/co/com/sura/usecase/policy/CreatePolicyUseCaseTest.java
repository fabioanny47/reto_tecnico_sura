package co.com.sura.usecase.policy;

import co.com.sura.model.enums.PolicyTypes;
import co.com.sura.model.events.gateways.EventPublisher;
import co.com.sura.model.gateways.PolicyRepository;
import co.com.sura.model.policy.Policy;
import co.com.sura.r2dbc.exception.EventPublishingException;
import events.DomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePolicyUseCaseTest {

    @Mock
    PolicyRepository policyRepository;
    @Mock
    EventPublisher  eventPublisher;

    CreatePolicyUseCase createPolicyUseCase;

    @BeforeEach
    void setUp() {
        createPolicyUseCase = new CreatePolicyUseCase(policyRepository, eventPublisher);
    }


    @Test
    void mustExecuteSuccessfully() {
        Policy policy = Policy.builder()
                .id(UUID.randomUUID())
                .policyId("policyTest")
                .amount(BigDecimal.valueOf(100))
                .fechaInicio(LocalDate.now().plusDays(1))
                .type(PolicyTypes.AUTO)
                .build();

        when(policyRepository.save(any(Policy.class))).thenReturn(Mono.just(policy));
        when(eventPublisher.publish(any(DomainEvent.class))).thenReturn(Mono.empty());

        Mono<Policy> result = createPolicyUseCase.execute(policy);

        StepVerifier.create(result)
                .expectNext(policy)
                .verifyComplete();
    }

    @Test
    void mustThrowEventPublishingException() {
        Policy policy = Policy.builder()
                .id(UUID.randomUUID())
                .policyId("policyTest")
                .amount(BigDecimal.valueOf(100))
                .fechaInicio(LocalDate.now().plusDays(1))
                .type(PolicyTypes.AUTO)
                .build();

        when(policyRepository.save(any(Policy.class))).thenReturn(Mono.just(policy));

        when(eventPublisher.publish(any(DomainEvent.class)))
                .thenReturn(Mono.error(new RuntimeException("RabbitMQ error")));

        Mono<Policy> result = createPolicyUseCase.execute(policy);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof EventPublishingException &&
                        throwable.getMessage().contains("No se pudo emitir el evento de creación de Policy"))
                .verify();
    }


    Policy getPolicy(){
        return Policy.builder()
                .id(UUID.randomUUID())
                .policyId("policyTest")
                .amount(BigDecimal.valueOf(1000))
                .fechaInicio(LocalDate.now())
                .type(PolicyTypes.AUTO)
                .build();
    }
}