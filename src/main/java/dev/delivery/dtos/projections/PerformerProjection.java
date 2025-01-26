package dev.delivery.dtos.projections;

import dev.delivery.enums.Role;

public interface PerformerProjection {
    Long getId();

    String getPhone();

    Role getRole();
}
