package co.com.sura.usecase.policy;

import co.com.sura.model.gateways.PolicyRepository;
import co.com.sura.model.policy.Policy;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdatePolicyUseCase {

    private final PolicyRepository policyRepository;

    public Mono<Policy> execute(Policy policy) {
        return policyRepository.save(policy);
    }
}
