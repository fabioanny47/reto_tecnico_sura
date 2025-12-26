package co.com.sura.r2dbc.adapter;

import co.com.sura.model.policy.Policy;
import co.com.sura.model.gateways.PolicyRepository;
import co.com.sura.r2dbc.entity.PolicyEntity;
import co.com.sura.r2dbc.exception.DatabaseConnectionException;
import co.com.sura.r2dbc.exception.NotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import co.com.sura.r2dbc.repository.R2dbcPolicyRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class policyRepositoryAdapter implements PolicyRepository{

    private final R2dbcPolicyRepository repository;

    @Override
    public Flux<Policy> findAll() {
        return repository.findAll()

                .switchIfEmpty(Mono.error(new NotFoundException("No se encontraron datos")))
                .map(p -> Policy.builder()
                        .id(p.getId())
                        .policyId( p.getPolicyId())
                        .amount(p.getAmount())
                        .fechaInicio(p.getFechaInicio())
                        .type(p.getType()).build());
    }

    @Override
    public Mono<Policy> save(Policy policy) {
        return repository.save(toEntity(policy))
                .map(this::toDomain)
                .onErrorMap(ex -> {
                    if (ex instanceof DataAccessResourceFailureException || ex instanceof DataAccessResourceFailureException) {
                        return new DatabaseConnectionException(ex);
                    }
                    return ex;
                });
    }


    @Override
    public Mono<Policy> findById(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("La politica que busca no existe")))
                .map(p -> Policy.builder()
                .id(p.getId())
                .policyId( p.getPolicyId())
                .amount(p.getAmount())
                .fechaInicio(p.getFechaInicio())
                .type(p.getType()).build());
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("La politica que intenta eliminar no existe")))
                .flatMap(policy ->
                        repository.deleteById(policy.getId())
                )
                .onErrorMap(ex -> {
                    System.out.print(ex);
                    if (ex instanceof DataAccessResourceFailureException || ex instanceof DataAccessResourceFailureException) {
                        return new DatabaseConnectionException(ex);
                    }
                    return ex;
                });
    }

    private PolicyEntity toEntity(Policy policy) {
        return PolicyEntity.builder()
                .id(policy.getId())
                .policyId(policy.getPolicyId())
                .fechaInicio(policy.getFechaInicio())
                .type(policy.getType())
                .amount(policy.getAmount())
                .build();
    }

    private Policy toDomain(PolicyEntity entity) {
        return Policy.builder()
                .id(entity.getId())
                .policyId( entity.getPolicyId())
                .amount(entity.getAmount())
                .fechaInicio(entity.getFechaInicio())
                .type(entity.getType()).build();
    }
}
