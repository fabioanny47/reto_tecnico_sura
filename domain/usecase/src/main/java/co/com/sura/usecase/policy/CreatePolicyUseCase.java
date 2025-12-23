package co.com.sura.usecase.policy;

import co.com.sura.model.policy.Policy;
import co.com.sura.model.gateways.PolicyRepository;
import co.com.sura.r2dbc.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class CreatePolicyUseCase {

    private final PolicyRepository policyRepository;

    LocalDate today = LocalDate.now();
    public Mono<Policy> execute(Policy policy) {

        if (!(policy.getAmount().compareTo(BigDecimal.ZERO) > 0)) {
            return Mono.error(new ValidationException("El monto debe ser mayor a 0"));
        }

        if (policy.getFechaInicio() == null) {
            return Mono.error(new ValidationException("La fecha es obligatoria"));
        }

        if (policy.getFechaInicio().isBefore(today)) {
            return Mono.error(new ValidationException("La fecha no debe ser anterior a la fecha actual"));
        }

        return policyRepository.save(policy);
    }
}
