package co.com.sura.entrypoint.mapper;

import co.com.sura.model.enums.PolicyTypes;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePolicyRequest(
        String policyId,
        PolicyTypes type,
        LocalDate fechaInicio,
        BigDecimal amount
) {}