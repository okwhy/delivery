package dev.delivery.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link dev.delivery.entities.AddressEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressInfoDto implements Serializable {
    @NotNull(message = "Широта обязательна")
    @DecimalMin(value = "-90", message = "Широта не может быть меньше -90")
    @DecimalMax(value = "90", message = "Широта не может быть больше 90")
    private double latitude;
    @NotNull(message = "Долгота обязательна")
    @DecimalMin(value = "-180", message = "Долгота не может быть меньше -180")
    @DecimalMax(value = "180", message = "Долгота не может быть больше 180")
    private double longitude;
    private String address;
}