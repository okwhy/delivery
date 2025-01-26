package dev.delivery.services;

import dev.delivery.dtos.ProductInCatalogDto;
import dev.delivery.dtos.TagDto;
import dev.delivery.entities.ProductEntity;
import dev.delivery.enums.Availability;
import dev.delivery.mappers.ProductInCatalogMapper;
import dev.delivery.repos.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductInCatalogMapper productInCatalogMapper;

    public Page<ProductInCatalogDto> getProducts(List<String> tags, String filter, Pageable pageable) {
        Page<ProductEntity> productEntities = productRepo
                .findByTagsAndDesc(Availability.IN_STOCK,
                        tags, filter, pageable);
        return productEntities.map(productInCatalogMapper::toDto);
    }

    public Page<ProductInCatalogDto> getProducts(String filter, Pageable pageable) {
        Page<ProductEntity> productEntities = productRepo
                .findByDesc(Availability.IN_STOCK, filter, pageable);
        return productEntities.map(productInCatalogMapper::toDto);
    }
}
