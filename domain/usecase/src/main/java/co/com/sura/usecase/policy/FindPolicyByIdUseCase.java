package co.com.sura.usecase.policy;

import co.com.sura.model.gateways.PolicyRepository;
import co.com.sura.model.policy.Policy;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class FindPolicyByIdUseCase {

    private final PolicyRepository policyRepository;

    public Mono<Policy> execute(UUID id) {
        return policyRepository.findById(id);
    }
}
