package dev.delivery.dtos;

import dev.delivery.dtos.projections.PerformerProjection;
import dev.delivery.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderActivityCreateDto {
    private Long order;
    private Status status;
    private String description;
}
