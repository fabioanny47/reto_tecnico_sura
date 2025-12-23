package co.com.sura.r2dbc;

import co.com.sura.model.enums.PolicyTypes;
import co.com.sura.model.policy.Policy;
import co.com.sura.r2dbc.adapter.policyRepositoryAdapter;
import co.com.sura.r2dbc.entity.PolicyEntity;
import co.com.sura.r2dbc.repository.R2dbcPolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class policyRepositoryAdapterTest {

    @Mock
    R2dbcPolicyRepository repository;

    policyRepositoryAdapter repositoryAdapter;

    @Mock
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        repositoryAdapter = new policyRepositoryAdapter(repository);
    }

    @Test
    void mustSaveVaulue() {

        when(repository.save(any(PolicyEntity.class)))
                .thenReturn(Mono.just(getPolicyEntity()));

        Mono<Policy> result = repositoryAdapter.save(getPolicy());

        StepVerifier.create(result)
                .expectNextMatches(p ->
                        p.getPolicyId().equals("policyTest") &&
                                p.getAmount().equals(BigDecimal.valueOf(1000))
                )
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {

        when(repository.findAll())
                .thenReturn(Flux.just(getPolicyEntity()));

        when(mapper.map("test", Object.class)).thenReturn("test");

        Flux<Policy> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    /*
    @Test
    void mustFindByExample() {
        when(repositoryAdapter.findAll()).thenReturn(Flux.just(getPolicy()));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Flux<PolicyEntity> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        when(repositoryAdapter.save(getPolicy())).thenReturn(Mono.just(getPolicy()));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Mono<PolicyEntity> result = repositoryAdapter.save(getPolicyEntity());

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }
*/
    Policy getPolicy() {
        return Policy.builder()
                .id(UUID.randomUUID())
                .policyId("policyTest")
                .amount(BigDecimal.valueOf(1000))
                .fechaInicio(LocalDate.now())
                .type(PolicyTypes.AUTO)
                .build();
    }

    PolicyEntity getPolicyEntity() {
        return PolicyEntity.builder()
                .id(UUID.randomUUID())
                .policyId(getPolicy().getPolicyId())
                .amount(getPolicy().getAmount())
                .fechaInicio(getPolicy().getFechaInicio())
                .type(getPolicy().getType())
                .build();
    }
 }
