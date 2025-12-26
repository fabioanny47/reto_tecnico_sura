package co.com.sura.r2dbc.entity;

import co.com.sura.model.enums.PolicyTypes;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Table("policy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PolicyEntity {
    @Id
    private UUID id;

    private String policyId ;

    private PolicyTypes type;

    private LocalDate fechaInicio;

    private BigDecimal amount;
}
