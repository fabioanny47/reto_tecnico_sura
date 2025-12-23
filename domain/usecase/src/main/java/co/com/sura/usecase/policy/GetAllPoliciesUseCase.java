package co.com.sura.usecase.policy;

import co.com.sura.model.policy.Policy;
import co.com.sura.model.gateways.PolicyRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetAllPoliciesUseCase {

    private final PolicyRepository policyRepository;

    public Flux<Policy> execute() {
        return policyRepository.findAll();
    }
}
