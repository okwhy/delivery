package dev.delivery.dtos;

import dev.delivery.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderForClientWithStatusDto {
    private String comment;
    private AddressInfoDto address;
    private Set<ProductItemDto> products = new LinkedHashSet<>();
    private Status status;
}
