package dev.delivery.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * DTO for {@link dev.delivery.entities.OrderEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderForClientDto implements Serializable {
    @NotNull(message = "Клиент обязателен")
    @Valid
    private ClientDto client;
    private String comment;
    @NotNull(message = "Адрес обязателен")
    @Valid
    private AddressInfoDto address;
    @NotNull(message = "Товары обязательны")
    @Valid
    private Set<ProductItemInfoDto> products = new LinkedHashSet<>();
}