package dev.delivery.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link dev.delivery.entities.ProductItemEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemInfoDto implements Serializable {
    @NotNull(message = "Количество товара обязательно")
    @DecimalMin(value = "1", message = "Количество должно быть положительным")
    private Integer quantity;
    private ProductInOrderDto product;
}