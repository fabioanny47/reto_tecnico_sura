package co.com.sura.entrypoint;

import co.com.sura.entrypoint.mapper.CreatePolicyRequest;
import co.com.sura.entrypoint.mapper.UpdatePolicyRequest;
import co.com.sura.model.policy.Policy;
import co.com.sura.usecase.policy.*;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Obtener todas las politicas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de politicas")
    })
    @GetMapping("/getAllPolicies")
    public Flux<Policy> getAllPolicies() {
        return getAllPoliciesUseCase.execute();
    }

    @Operation(summary = "Obtener politica por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "politica unitaria por id")
    })
    @GetMapping("/getPolicyById/{id}")
    public Mono<Policy> findPolicyById(@PathVariable("id") UUID id) {
        return findPolicyByIdUseCase.execute(id);
    }

    @Operation(summary = "Obtener politica por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "politica unitaria por id")
    })
    @DeleteMapping("/deletePolicyById/{id}")
    public Mono<Policy> deletePolicyById(@PathVariable("id") UUID id) {
        return deletePolicyUseCase.execute(id);
    }

    @Operation(summary = "crear nueva politica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "politica creada con exito")
    })
    @PostMapping("/createPolicy")
    public Mono<Policy> createPolicy(@RequestBody CreatePolicyRequest request) {
        log.info("Request: {}", request);
        return createPoliciesUseCase.execute( Policy.builder()
                .policyId(request.policyId())
                .amount(request.amount())
                .fechaInicio(request.fechaInicio())
                .type(request.type()).build());
    }

    @Operation(summary = "actualizar politica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "politica actaulizada con exito")
    })
    @PutMapping("/updatePolicy")
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
