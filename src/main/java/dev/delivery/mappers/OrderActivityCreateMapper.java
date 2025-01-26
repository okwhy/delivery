package dev.delivery.mappers;

import dev.delivery.dtos.OrderActivityCreateDto;
import dev.delivery.entities.OrderActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface OrderActivityCreateMapper {
    OrderActivityCreateMapper INSTANCE = Mappers.getMapper(OrderActivityCreateMapper.class);

    @Mapping(source = "order.id", target = "order")
    OrderActivityCreateDto toDto(OrderActivityEntity orderActivity);

    @Mapping(source = "order", target = "order.id")
    OrderActivityEntity toEntity(OrderActivityCreateDto orderActivityCreateDto);
}
