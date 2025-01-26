package dev.delivery.mappers;

import dev.delivery.dtos.OrderForClientWithStatusDto;
import dev.delivery.entities.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, ProductItemMapper.class})
public interface OrderForClientMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "address", target = "address")
    @Mapping(source = "products", target = "products")
    OrderForClientWithStatusDto toDto(OrderEntity orderEntity);
}
