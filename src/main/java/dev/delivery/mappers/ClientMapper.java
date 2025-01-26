package dev.delivery.mappers;

import dev.delivery.dtos.ClientDto;
import dev.delivery.entities.ClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    ClientDto toDto(ClientEntity clientEntity);

    ClientEntity toEntity(ClientDto clientDto);
}
