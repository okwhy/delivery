package dev.delivery.repos;

import dev.delivery.dtos.ClientDto;
import dev.delivery.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepo extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("select c from ClientEntity c where c.phoneNumber = ?1")
    ClientEntity getReferenceByPhoneNumber(String phoneNumber);
}