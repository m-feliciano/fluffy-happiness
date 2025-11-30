package br.com.microservices.notification.controller.openapi;

import br.com.microservices.clients.notification.NotificationRequest;
import br.com.microservices.notification.domain.response.NotificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Notification", description = "API for notification management")
@SecurityRequirement(name = "Bearer Authentication")
public interface NotificationControllerOpenApi {

    @Operation(summary = "Send notification", description = "Sends a new notification to a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @Parameter(
            name = "Authorization",
            description = "Bearer token for authentication",
            hidden = true,
            in = ParameterIn.HEADER
    )
    ResponseEntity<NotificationResponse> send(@RequestBody(description = "Notification data to send") NotificationRequest notificationRequest,
                                              @RequestHeader(name = "Authorization", required = false) String authorization);

    @Operation(summary = "List all notifications", description = "Returns a paginated list of all notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification list returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @Parameter(
            name = "Authorization",
            description = "Bearer token for authentication",
            hidden = true,
            in = ParameterIn.HEADER
    )
    Page<NotificationResponse> findAll(@ParameterObject Pageable pageRequest,
                                       @RequestHeader(name = "Authorization", required = false) String authorization);
}

