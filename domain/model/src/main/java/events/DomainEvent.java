package events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class DomainEvent<T> {
    private final String name;
    private final UUID id;
    private final T payload;
}
