package dev.delivery.mappers;

import dev.delivery.dtos.OrderActivityDto;
import dev.delivery.entities.OrderActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {PerformerMapper.class})
public interface OrderActivityMapper {
    OrderActivityMapper INSTANCE = Mappers.getMapper(OrderActivityMapper.class);

    OrderActivityDto toDto(OrderActivityEntity orderActivity);

    OrderActivityEntity toEntity(OrderActivityDto dto);
}
