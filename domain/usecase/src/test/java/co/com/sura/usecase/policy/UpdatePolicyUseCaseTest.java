package co.com.sura.usecase.policy;

import co.com.sura.model.enums.PolicyTypes;
import co.com.sura.model.gateways.PolicyRepository;
import co.com.sura.model.policy.Policy;
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
class UpdatePolicyUseCaseTest {

    @Mock
    PolicyRepository policyRepository;

    UpdatePolicyUseCase   updatePolicyUseCase;

    @BeforeEach
    void setUp() {
        updatePolicyUseCase = new UpdatePolicyUseCase(policyRepository);
    }

    @Test
    void execute() {

        Policy policy = Policy.builder()
                .id(UUID.randomUUID())
                .policyId("policyTest")
                .amount(BigDecimal.valueOf(1000))
                .fechaInicio(LocalDate.now())
                .type(PolicyTypes.AUTO)
                .build();

        when(policyRepository.save(any(Policy.class))).thenReturn(Mono.just(getPolicy()));
        Mono<Policy> result =  updatePolicyUseCase.execute(policy);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getPolicyId().equals("policyTest"))
                .verifyComplete();

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