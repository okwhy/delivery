package dev.delivery.controllers;

import dev.delivery.dtos.ClientDto;
import dev.delivery.dtos.OrderForClientDto;
import dev.delivery.dtos.OrderForClientWithStatusDto;
import dev.delivery.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "C. Order API", description = "API для просмотра своих заказов клиентом и создания нового заказа")
public class OrderClientSideController {
    private final OrderService orderService;

    @Operation(summary = "Создать заказ")
    @PostMapping("/api/order")
    public OrderForClientWithStatusDto createOrder(@Valid @RequestBody OrderForClientDto order) {
        return orderService.createOrder(order);
    }

    @Operation(summary = "Посмотреть мои заказы")
    @GetMapping("/api/myorders")
    public List<OrderForClientWithStatusDto> getMyOrders(@Valid @RequestParam("fio") String fio,
                                                         @RequestParam("phoneNumber") String phoneNumber
    ) {
        ClientDto client = new ClientDto(fio, phoneNumber);
        return orderService.getOrdersByClient(client);
    }
}
