package dev.delivery.repos;

import dev.delivery.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepo extends JpaRepository<TagEntity, Long> {
}