package dev.delivery.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link dev.delivery.entities.ClientEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto implements Serializable {
    @NotBlank(message = "ФИО обязателен")
    private String fio;
    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^\\+7\\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}$", message = "Телефон должен быть в формате +7(XXX) XXX-XX-XX")
    private String phoneNumber;
}