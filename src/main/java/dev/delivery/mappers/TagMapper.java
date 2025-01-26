package dev.delivery.mappers;

import dev.delivery.dtos.TagDto;
import dev.delivery.entities.TagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    TagDto toDto(TagEntity tag);

    TagEntity toEntity(TagDto dto);
}
