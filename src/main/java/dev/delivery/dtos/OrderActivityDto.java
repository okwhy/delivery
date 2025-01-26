package dev.delivery.dtos;

import dev.delivery.dtos.projections.PerformerProjection;
import dev.delivery.dtos.projections.PerformerProjectionImpl;
import dev.delivery.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link dev.delivery.entities.OrderActivityEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderActivityDto implements Serializable {
    private Long id;
    private LocalDateTime timestamp;
    private String description;
    private Status status;
    private PerformerProjectionImpl performer;
}