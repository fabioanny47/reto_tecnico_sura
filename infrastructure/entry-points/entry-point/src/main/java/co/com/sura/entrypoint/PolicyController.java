package co.com.sura.entrypoint;

import co.com.sura.entrypoint.mapper.CreatePolicyRequest;
import co.com.sura.entrypoint.mapper.UpdatePolicyRequest;
import co.com.sura.exception.ErrorResponse;
import co.com.sura.model.policy.Policy;
import co.com.sura.usecase.policy.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;
import static reactor.netty.http.HttpConnectionLiveness.log;

@RestController
@RequestMapping("/api/policy")
@RequiredArgsConstructor
public class PolicyController {

    private final GetAllPoliciesUseCase getAllPoliciesUseCase;

    private final UpdatePolicyUseCase updatePolicyUseCase;

    private final CreatePolicyUseCase createPoliciesUseCase;

    private final FindPolicyByIdUseCase findPolicyByIdUseCase;

    private final DeletePolicyUseCase deletePolicyUseCase;

    @Operation(summary = "Obtener todas las politicas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de politicas")
    })
    @GetMapping("/policies")
    public Flux<Policy> getAllPolicies() {
        return getAllPoliciesUseCase.execute();
    }


    @Operation(summary = "Obtener politica por id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos para crear una politica"
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "politica unitaria por id")
    })
    @GetMapping("/policy/{id}")
    public Mono<Policy> findPolicyById(@PathVariable("id") UUID id) {
        return findPolicyByIdUseCase.execute(id);
    }


    @Operation(summary = "Eliminar póliza por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Póliza eliminada"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Póliza no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/policy/{id}")
    public Mono<Void> deletePolicyById(@PathVariable("id") UUID id) {
        return deletePolicyUseCase.execute(id);
    }

    @Operation(
            summary = "Crear nueva póliza",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos para crear una póliza",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreatePolicyRequest.class),
                            examples = @ExampleObject(
                                    name = "CreatePolicyExample",
                                    summary = "Ejemplo de creación de póliza",
                                    value = """
                { 
                  "policyId": "POL-12345",
                  "amount": 1500000,
                  "fechaInicio": "2025-04-01",
                  "type": "AUTO"
                }
                """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "politica creada con exito")
    })
    @PostMapping("/policy")
    public Mono<Policy> createPolicy(@RequestBody CreatePolicyRequest request) {
        log.info("Request: {}", request);
        return createPoliciesUseCase.execute( Policy.builder()
                .policyId(request.policyId())
                .amount(request.amount())
                .fechaInicio(request.fechaInicio())
                .type(request.type()).build());
    }

    @Operation(
            summary = "Actualizar póliza",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos para actualizar una póliza",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdatePolicyRequest.class),
                            examples = @ExampleObject(
                                    name = "UpdatePolicyExample",
                                    value = """
                                    {
                                      "id": "550e8400-e29b-41d4-a716-446655440000",
                                      "policyId": "POL-12345",
                                      "amount": 2000000,
                                      "fechaInicio": "2025-05-01",
                                      "type": "AUTO"
                                    }
                                    """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "politica actaulizada con exito")
    })
    @PutMapping("/policy")
    public Mono<Policy> updatePolicy(@RequestBody UpdatePolicyRequest request) {
        log.info("Request: {}", request);
        return updatePolicyUseCase.execute( Policy.builder()
                .id(request.id())
                .policyId(request.policyId())
                .amount(request.amount())
                .fechaInicio(request.fechaInicio())
                .type(request.type()).build());
    }
}
