package dev.delivery.repos;

import dev.delivery.entities.CredentialEntity;
import org.springframework.data.repository.CrudRepository;

public interface CredentialRepo extends CrudRepository<CredentialEntity, Long> {
}