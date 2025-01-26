package dev.delivery.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link dev.delivery.entities.AddressEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto implements Serializable {
    private Long id;
    private double latitude;
    private double longitude;
    private String address;
}