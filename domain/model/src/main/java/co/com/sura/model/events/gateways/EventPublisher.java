package co.com.sura.model.events.gateways;

import events.DomainEvent;
import reactor.core.publisher.Mono;

public interface EventPublisher {
    Mono<Void> publish(DomainEvent<?> event);
}
