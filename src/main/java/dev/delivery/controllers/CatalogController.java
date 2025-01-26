package dev.delivery.controllers;

import dev.delivery.dtos.ProductInCatalogDto;
import dev.delivery.dtos.TagDto;
import dev.delivery.services.ProductService;
import dev.delivery.services.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/products",produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "B. Catalog API", description = "API для отображения продуктов из каталога и их тегов")
public class CatalogController {
    private final ProductService productService;
    private final TagService tagService;

    @Operation(summary = "Получение всех тегов")
    @GetMapping(value = "/tags")
    public List<TagDto> getTags() {
        return tagService.getAllTags();
    }

    @Operation(summary = "Получение продуктов")
    @GetMapping()
    @PageableAsQueryParam
    public Page<ProductInCatalogDto> getProducts(@RequestParam(required = false) String description,
                                                 @Parameter(hidden = true)
                                                 @PageableDefault(sort = "id") Pageable pageable,
                                                 @Parameter(description = "При не указывании тегов выводятся товары по всем тегам")
                                                 @RequestParam(required = false) List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return productService.getProducts(description, pageable);
        }
        return productService.getProducts(tags, description, pageable);
    }


}
