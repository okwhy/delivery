package dev.delivery.dtos;

import dev.delivery.dtos.projections.PerformerProjection;
import dev.delivery.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link dev.delivery.entities.PerformerEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerDto implements PerformerProjection {
    private Long id;
    private String phone;
    private Role role = Role.MANAGER;
}