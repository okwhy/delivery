package dev.delivery.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link dev.delivery.entities.CredentialEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialDto {
    private String username;
    private String password;
}