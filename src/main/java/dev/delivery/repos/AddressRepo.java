package dev.delivery.repos;

import dev.delivery.entities.AddressEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepo extends CrudRepository<AddressEntity, Long> {
    @Query("select a from AddressEntity a where a.latitude = ?1 and a.longitude = ?2")
    AddressEntity findByLocation(double latitude, double longitude);
}