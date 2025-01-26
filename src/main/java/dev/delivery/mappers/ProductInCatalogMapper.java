package dev.delivery.mappers;

import dev.delivery.dtos.ProductInCatalogDto;
import dev.delivery.dtos.TagDto;
import dev.delivery.entities.ProductEntity;
import dev.delivery.entities.ProductTagEntity;
import dev.delivery.entities.TagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductInCatalogMapper {
    ProductInCatalogMapper INSTANCE = Mappers.getMapper(ProductInCatalogMapper.class);

    @Mapping(source = "productTags", target = "tags")
    ProductInCatalogDto toDto(ProductEntity product);

    List<ProductInCatalogDto> toDtoList(List<ProductEntity> products);

    default List<TagDto> mapTags(List<ProductTagEntity> tags) {
        return tags.stream()
                .map(productTag -> {
                    TagDto tagDto = new TagDto();
                    tagDto.setTag(productTag.getTag().getTag());
                    return tagDto;
                })
                .toList();
    }
}
