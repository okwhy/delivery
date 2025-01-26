package dev.delivery.repos;

import dev.delivery.entities.BoxEntity;
import org.springframework.data.repository.CrudRepository;

public interface BoxRepo extends CrudRepository<BoxEntity, Long> {
}