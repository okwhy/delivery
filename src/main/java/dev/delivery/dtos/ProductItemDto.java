package dev.delivery.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemDto implements Serializable {
    private Integer quantity;
    private ProductDto product;
}
