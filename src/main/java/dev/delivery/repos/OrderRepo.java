package dev.delivery.repos;

import dev.delivery.entities.ClientEntity;
import dev.delivery.entities.OrderEntity;
import dev.delivery.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepo extends JpaRepository<OrderEntity, Long> {
    @Query("SELECT o FROM OrderEntity o WHERE o.client.phoneNumber = :phone")
    List<OrderEntity> findByClientPhone(@Param("phone") String phone);

    @Query("""
            select distinct o from OrderEntity o inner join o.orderActivityEntities orderActivityEntities
            where orderActivityEntities.status = ?1""")
    Page<OrderEntity> findByPresentStatus(Status status,Pageable pageable);

    @Query("SELECT DISTINCT o FROM OrderEntity o WHERE o.orderActivityEntities IS EMPTY")
    Page<OrderEntity> findWithoutStatus(Pageable pageable);

    @Query("""
            select o from OrderEntity o
            where exists (
                select 1 from OrderActivityEntity a
                where a.order = o
                  and a.timestamp = (
                    select max(a2.timestamp)
                    from OrderActivityEntity a2
                    where a2.order = o
                  )
                  and a.status in :statuses
            )
            """)
    Page<OrderEntity> findByPresentStatuses(@Param("statuses") List<Status> statuses, Pageable pageable);
}