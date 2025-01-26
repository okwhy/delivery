package dev.delivery.repos;

import dev.delivery.entities.ProductEntity;
import dev.delivery.enums.Availability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ProductRepo extends JpaRepository<ProductEntity, Long> {
    @Query("""
            select p from ProductEntity p inner join p.productTags productTags
            where p.availability = :availability and productTags.tag.tag in :tags and
            (:desc is null or :desc = '' or LOWER(p.description) LIKE LOWER(CONCAT('%',:desc, '%')))""")
    Page<ProductEntity> findByTagsAndDesc(@Param("availability") Availability availability,
                                          @Param("tags") Collection<String> tags,
                                          @Param("desc") String desc,
                                          Pageable pageable);

    @Query("""
            select p from ProductEntity p
            where p.availability = :availability  and
            (:desc is null or :desc = '' or LOWER(p.description) LIKE LOWER(CONCAT('%',:desc, '%')))""")
    Page<ProductEntity> findByDesc(@Param("availability") Availability availability,
                                   @Param("desc") String desc,
                                   Pageable pageable);

    boolean existsByIdAndAvailability(Long id, Availability availability);

}