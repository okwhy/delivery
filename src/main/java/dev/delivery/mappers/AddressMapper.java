package dev.delivery.mappers;

import dev.delivery.dtos.AddressInfoDto;
import dev.delivery.entities.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    AddressInfoDto toDto(AddressEntity addressEntity);

    AddressEntity toEntity(AddressInfoDto addressDto);
}
