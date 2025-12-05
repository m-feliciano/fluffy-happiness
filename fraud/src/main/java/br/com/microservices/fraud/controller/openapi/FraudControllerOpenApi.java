package br.com.microservices.fraud.controller.openapi;

import br.com.microservices.clients.fraud.FraudCheckResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Fraud Check", description = "API for fraud verification")
@SecurityRequirement(name = "Bearer Authentication")
public interface FraudControllerOpenApi {

    @Operation(summary = "Check fraud", description = "Checks if a customer is potentially fraudulent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification completed successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    FraudCheckResponse isFraudster(
            @Parameter(description = "Customer ID to be verified", required = true)
            Long customerId
    );
}

