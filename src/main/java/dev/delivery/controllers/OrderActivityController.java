package dev.delivery.controllers;

import dev.delivery.dtos.OrderActivityCreateDto;
import dev.delivery.dtos.OrderActivityDto;
import dev.delivery.dtos.OrderForClientWithStatusDto;
import dev.delivery.dtos.auth.AuthUser;
import dev.delivery.enums.Status;
import dev.delivery.services.OrderActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/employee/activities")
@AllArgsConstructor
@Tag(name = "E. Order Activity API", description = "API для работы сотрудников с заказами")
public class OrderActivityController {
    private final OrderActivityService orderActivityService;

    @Operation(summary = "Создать активность в заказе", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderActivityDto add(@RequestBody OrderActivityCreateDto data,
                                @AuthenticationPrincipal AuthUser authUser) {
        return orderActivityService.publishActivity(data, authUser.getId(), authUser.getAuthorities());
    }

    @Operation(summary = "Получить активности", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/")
    @PageableAsQueryParam
    public Page<OrderActivityDto> getActivities(@AuthenticationPrincipal AuthUser authUser,
                                                @RequestParam List<Status> statuses,
                                                @Parameter(hidden = true)
                                                @PageableDefault(sort = "id") Pageable pageable) {
        return orderActivityService.getActivities(statuses, authUser.getAuthorities(), authUser.getId(), pageable);
    }

    @Operation(summary = "Получить заказы по последней активности", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/orders")
    @PageableAsQueryParam
    public Page<OrderForClientWithStatusDto> getOrders(@AuthenticationPrincipal AuthUser authUser,

                                                       @RequestParam(required = false) List<Status> statuses, @Parameter(hidden = true)
                                                       @PageableDefault(sort = "id") Pageable pageable) {
        return orderActivityService.getOrdersByStatus(statuses, authUser.getAuthorities(), authUser.getId(), pageable);
    }
}
