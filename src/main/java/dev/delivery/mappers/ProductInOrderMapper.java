package dev.delivery.mappers;

import dev.delivery.dtos.ProductInOrderDto;
import dev.delivery.entities.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductInOrderMapper {
    ProductInOrderMapper INSTANCE = Mappers.getMapper(ProductInOrderMapper.class);

    ProductInOrderDto toDTO(ProductEntity productEntity);

    ProductEntity toEntity(ProductInOrderDto productInOrderDto);
}
