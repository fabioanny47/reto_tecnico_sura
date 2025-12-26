package co.com.sura.usecase.policy;

import co.com.sura.model.events.gateways.EventPublisher;
import co.com.sura.model.gateways.PolicyRepository;
import co.com.sura.model.policy.Policy;
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

        //nombres dicientes cambiar p , metdodos de no mas de 15 lienas
        return Mono.just(policy)
                .filter(p -> p.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .switchIfEmpty(Mono.error(new ValidationException("El monto debe ser mayor a 0")))
                .filter(p -> p.getFechaInicio() != null)
                .switchIfEmpty(Mono.error(new ValidationException("La fecha es obligatoria")))
                .filter(p -> !p.getFechaInicio().isBefore(today))
                .switchIfEmpty(Mono.error(new ValidationException("La fecha no debe ser anterior a la fecha actual")))
                .flatMap(policyRepository::save)
                .flatMap(savedPolicy -> {
                    DomainEvent<Policy> event = new DomainEvent<>(
                            "policy.created",
                            UUID.randomUUID(),
                            savedPolicy
                    );
                    return eventPublisher.publish(event)
                            .then(Mono.just(savedPolicy))
                            .onErrorMap(e -> new EventPublishingException(
                                    "No se pudo emitir el evento de creación de Politica: " + e.getMessage(), e
                            ));
                });
    }
}
