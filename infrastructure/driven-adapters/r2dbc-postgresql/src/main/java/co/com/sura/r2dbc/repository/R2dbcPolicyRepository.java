package co.com.sura.r2dbc.repository;

import co.com.sura.r2dbc.entity.PolicyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface R2dbcPolicyRepository extends ReactiveCrudRepository<PolicyEntity, UUID> {
}
