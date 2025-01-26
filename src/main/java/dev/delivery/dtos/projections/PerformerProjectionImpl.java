package dev.delivery.dtos.projections;

import dev.delivery.enums.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerformerProjectionImpl implements PerformerProjection {
    private Long id;
    private String phone;
    private Role role;
}
