package dev.delivery.mappers;

import dev.delivery.dtos.projections.PerformerProjection;
import dev.delivery.dtos.projections.PerformerProjectionImpl;
import dev.delivery.entities.PerformerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PerformerMapper {
    PerformerMapper INSTANCE = Mappers.getMapper(PerformerMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "role", target = "role")
    PerformerProjectionImpl toProjection(PerformerEntity performer);
}