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
class DeletePolicyUseCaseTest {

    @Mock
    PolicyRepository policyRepository;

    DeletePolicyUseCase deletePolicyUseCase;

    @BeforeEach
    void setUp() {
        deletePolicyUseCase = new DeletePolicyUseCase(policyRepository);
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

        when(policyRepository.deleteById(any(UUID.class))).thenReturn(Mono.empty());
        Mono<Void> result = deletePolicyUseCase.execute(UUID.randomUUID());

        StepVerifier.create(result)
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