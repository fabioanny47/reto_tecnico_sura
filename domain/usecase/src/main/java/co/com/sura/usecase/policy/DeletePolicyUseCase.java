package co.com.sura.usecase.policy;

import co.com.sura.model.policy.Policy;
import co.com.sura.model.gateways.PolicyRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class DeletePolicyUseCase {

    private final PolicyRepository policyRepository;

    public Mono<Policy> execute(UUID id) {
        return policyRepository.deleteById(id);
    }
}
