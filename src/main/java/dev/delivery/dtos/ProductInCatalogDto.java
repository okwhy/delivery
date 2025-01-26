package dev.delivery.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInCatalogDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private List<TagDto> tags;
}
