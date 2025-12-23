package co.com.sura.model.gateways;

import co.com.sura.model.policy.Policy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PolicyRepository {
    Flux<Policy> findAll();
    Mono<Policy> save(Policy policy);
    Mono<Policy> findById(UUID id);
    Mono<Policy> deleteById(UUID id);

}
