package br.com.microservices.customer.controller.openapi;

import br.com.microservices.customer.domain.request.CustomerRequest;
import br.com.microservices.customer.domain.response.CustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "Customer", description = "API for customer management")
@SecurityRequirement(name = "Bearer Authentication")
public interface CustomerControllerOpenApi {

    @Operation(summary = "Register new customer", description = "Creates a new customer in the system and sends it for fraud verification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    ResponseEntity<CustomerResponse> register(@RequestBody(description = "Customer data to register") CustomerRequest customerRequest);

    @Operation(summary = "List all customers", description = "Returns a paginated list of all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer list returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    Page<CustomerResponse> findAll(@ParameterObject Pageable pageRequest);
}

