package co.com.sura.usecase.policy;

import co.com.sura.model.events.gateways.EventPublisher;
import co.com.sura.model.policy.Policy;
import co.com.sura.model.gateways.PolicyRepository;
import co.com.sura.r2dbc.exception.EventPublishingException;
import co.com.sura.r2dbc.exception.ValidationException;
import events.DomainEvent;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
public class CreatePolicyUseCase {

    private final PolicyRepository policyRepository;

    private final EventPublisher eventPublisher;

    public Mono<Policy> execute(Policy policy) {

        LocalDate today = LocalDate.now();

        if (!(policy.getAmount().compareTo(BigDecimal.ZERO) > 0)) {
            return Mono.error(new ValidationException("El monto debe ser mayor a 0"));
        }

        if (policy.getFechaInicio() == null) {
            return Mono.error(new ValidationException("La fecha es obligatoria"));
        }

        if (policy.getFechaInicio().isBefore(today)) {
            return Mono.error(new ValidationException("La fecha no debe ser anterior a la fecha actual"));
        }

        return policyRepository.save(policy)
                .flatMap(savedPolicy -> {
                    DomainEvent<Policy> event = new DomainEvent<>(
                            "policy.created",
                            UUID.randomUUID(),
                            savedPolicy
                    );

                    return eventPublisher.publish(event)
                            .then(Mono.just(savedPolicy))
                            .onErrorMap(e -> new EventPublishingException(
                                    "No se pudo emitir el evento de creación de Policy: " + e.getMessage(), e
                            ));
                });
    }
}
