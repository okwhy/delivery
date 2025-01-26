package dev.delivery.mappers;

import dev.delivery.dtos.OrderForClientDto;
import dev.delivery.entities.OrderEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, ProductInOrderMapper.class, ProductItemMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderForClientDto toDto(OrderEntity order);

    OrderEntity toEntity(OrderForClientDto dto);
}
