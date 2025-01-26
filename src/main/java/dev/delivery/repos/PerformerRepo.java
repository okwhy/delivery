package dev.delivery.repos;

import dev.delivery.dtos.projections.PerformerProjection;
import dev.delivery.entities.PerformerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PerformerRepo extends JpaRepository<PerformerEntity, Long> {
    @Query("select p from PerformerEntity p where p.credential.username = ?1")
    Optional<PerformerEntity> findByUsername(String username);

    @Query("select p from PerformerEntity p where p.id = ?1")
    Optional<PerformerProjection> findProjectionById(Long id);
}