package dev.delivery.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link dev.delivery.entities.ProductEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInOrderDto implements Serializable {
    private Long id;
}