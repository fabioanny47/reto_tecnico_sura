package co.com.sura.r2dbc;

import co.com.sura.model.enums.PolicyTypes;
import co.com.sura.model.policy.Policy;
import co.com.sura.r2dbc.adapter.policyRepositoryAdapter;
import co.com.sura.r2dbc.entity.PolicyEntity;
import co.com.sura.r2dbc.exception.DatabaseConnectionException;
import co.com.sura.r2dbc.repository.R2dbcPolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
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
    void mustThrowDatabaseConnectionExceptionWhenTimeoutOccurs() {

        Policy policy = Policy.builder()
                .id(UUID.randomUUID())
                .policyId("policyTest")
                .amount(BigDecimal.valueOf(1000))
                .fechaInicio(LocalDate.now())
                .type(PolicyTypes.AUTO)
                .build();

        when(repository.save(any(PolicyEntity.class)))
                .thenReturn(Mono.error(new DataAccessResourceFailureException("Timeout")));

        Mono<Policy> result = repositoryAdapter.save(policy);

        StepVerifier.create(result)
                .expectError(DatabaseConnectionException.class)
                .verify();
    }

    @Test
    void mustFindAllValues() {

        when(repository.findAll())
                .thenReturn(Flux.just(getPolicyEntity()));

        Flux<Policy> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getPolicyId().equals("policyTest"))
                .verifyComplete();
    }

    @Test
    void mustFindByIdExample() {

        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(getPolicyEntity()));

        Mono<Policy> result = repositoryAdapter.findById(UUID.randomUUID());

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getPolicyId().equals("policyTest"))
                .verifyComplete();
    }

    @Test
    void mustDeleteExample() {

        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(getPolicyEntity()));
        when(repository.deleteById(any(UUID.class))).thenReturn(Mono.empty());

        Mono<Void> result = repositoryAdapter.deleteById(UUID.randomUUID());

        StepVerifier.create(result)
                .verifyComplete();
    }


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
