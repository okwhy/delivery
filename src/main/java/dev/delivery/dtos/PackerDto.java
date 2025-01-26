package dev.delivery.dtos;

import dev.delivery.dtos.projections.PerformerProjection;
import dev.delivery.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackerDto implements PerformerProjection {
    private Long id;
    private String phone;
    private Role role = Role.PACKER;
}