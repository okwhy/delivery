package dev.delivery.mappers;

import dev.delivery.dtos.ProductItemInfoDto;
import dev.delivery.entities.ProductItemEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = ProductInOrderMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductItemMapper {
    ProductItemMapper INSTANCE = Mappers.getMapper(ProductItemMapper.class);

    ProductItemInfoDto toDto(ProductItemEntity productItemEntity);

    ProductItemEntity toEntity(ProductItemInfoDto productItemDto);
}
