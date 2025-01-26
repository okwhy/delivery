package dev.delivery.controllers;

import dev.delivery.dtos.ClientDto;
import dev.delivery.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "A. ClientAuth API", description = "API для аутентификации клиента")
public class ClientAuthController {
    private final ClientService clientService;

    @Operation(summary = "Вход как клиент")
    @PostMapping("/api/login")
    public ClientDto login(
            @Parameter(description = "Номер должен быть в формате +7(900) 000-00-00", required = true)
            @RequestParam("phone") String phone) {
        return clientService.login(phone);
    }

    @Operation(summary = "Регистрация нового клиента")
    @PostMapping("/api/register")
    public ClientDto register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody
                    (description = "Данные пользователя (номер должен быть в формате +7(900) 000-00-00)", required = true)
            @Valid @RequestBody ClientDto client) {
        return clientService.create(client);
    }
}
