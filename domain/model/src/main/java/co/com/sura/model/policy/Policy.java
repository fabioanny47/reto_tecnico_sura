package co.com.sura.model.policy;

import co.com.sura.model.enums.PolicyTypes;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Value
@Builder(toBuilder = true)
public class Policy {

    UUID id;

    String policyId ;

    PolicyTypes type;

    LocalDate fechaInicio;

    BigDecimal amount;
}
