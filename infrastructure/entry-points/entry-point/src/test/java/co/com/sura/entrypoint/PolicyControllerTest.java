package co.com.sura.entrypoint;

import co.com.sura.entrypoint.mapper.CreatePolicyRequest;
import co.com.sura.entrypoint.mapper.UpdatePolicyRequest;
import co.com.sura.model.enums.PolicyTypes;
import co.com.sura.model.policy.Policy;
import co.com.sura.usecase.policy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
class PolicyControllerTest {

    @Mock
    GetAllPoliciesUseCase getAllPoliciesUseCase;
    @Mock
    FindPolicyByIdUseCase findPolicyByIdUseCase;
    @Mock
    DeletePolicyUseCase deletePolicyUseCase;
    @Mock
    CreatePolicyUseCase createPoliciesUseCase;
    @Mock
    UpdatePolicyUseCase updatePolicyUseCase;

    PolicyController controller;

    @BeforeEach
    void setUp() {
        controller = new PolicyController(
                getAllPoliciesUseCase,
                updatePolicyUseCase,
                createPoliciesUseCase,
                findPolicyByIdUseCase,
                deletePolicyUseCase
        );
    }

    @Test
    void getAllPolicies_success() {
        Policy p1 = Policy.builder().policyId("POL-1").build();
        Policy p2 = Policy.builder().policyId("POL-2").build();

        when(getAllPoliciesUseCase.execute()).thenReturn(Flux.just(p1, p2));

        StepVerifier.create(controller.getAllPolicies())
                .expectNext(p1)
                .expectNext(p2)
                .verifyComplete();
    }

    @Test
    void findPolicyById_success() {
        UUID id = UUID.randomUUID();
        Policy policy = Policy.builder().id(id).policyId("POL-1").build();

        when(findPolicyByIdUseCase.execute(id)).thenReturn(Mono.just(policy));

        StepVerifier.create(controller.findPolicyById(id))
                .expectNext(policy)
                .verifyComplete();
    }

    @Test
    void findPolicyById_notFound() {
        UUID id = UUID.randomUUID();

        when(findPolicyByIdUseCase.execute(id)).thenReturn(Mono.empty());

        StepVerifier.create(controller.findPolicyById(id))
                .verifyComplete(); // Devuelve Mono vacío
    }

    @Test
    void deletePolicyById_success() {
        UUID id = UUID.randomUUID();

        when(deletePolicyUseCase.execute(id)).thenReturn(Mono.empty());

        StepVerifier.create(controller.deletePolicyById(id))
                .verifyComplete();
    }

    @Test
    void createPolicy_success() {
        CreatePolicyRequest request = new CreatePolicyRequest("POL-123", PolicyTypes.AUTO, LocalDate.now(), BigDecimal.valueOf(1000));
        Policy savedPolicy = Policy.builder()
                .policyId(request.policyId())
                .amount(request.amount())
                .fechaInicio(request.fechaInicio())
                .type(request.type())
                .build();

        when(createPoliciesUseCase.execute(any(Policy.class))).thenReturn(Mono.just(savedPolicy));

        StepVerifier.create(controller.createPolicy(request))
                .expectNext(savedPolicy)
                .verifyComplete();
    }

    @Test
    void updatePolicy_success() {
        UpdatePolicyRequest request = new UpdatePolicyRequest(UUID.randomUUID(), "POL-123", PolicyTypes.AUTO, LocalDate.now(), BigDecimal.valueOf(2000));
        Policy updatedPolicy = Policy.builder()
                .id(request.id())
                .policyId(request.policyId())
                .amount(request.amount())
                .fechaInicio(request.fechaInicio())
                .type(request.type())
                .build();

        when(updatePolicyUseCase.execute(any(Policy.class))).thenReturn(Mono.just(updatedPolicy));

        StepVerifier.create(controller.updatePolicy(request))
                .expectNext(updatedPolicy)
                .verifyComplete();
    }
}