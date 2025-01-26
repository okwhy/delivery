package dev.delivery.repos;

import dev.delivery.entities.OrderActivityEntity;
import dev.delivery.entities.OrderEntity;
import dev.delivery.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface OrderActivityRepo extends JpaRepository<OrderActivityEntity, Long> {
    @Query(value = "SELECT a FROM OrderActivityEntity a WHERE a.order = :order ORDER BY a.timestamp DESC")
    OrderActivityEntity findFirstByLatestActivity(@Param("order") OrderEntity order);

    Page<OrderActivityEntity> findByStatusIn(Collection<Status> statuses, Pageable pageable);

    Page<OrderActivityEntity> findByStatus(Status status, Pageable pageable);

    @Query("select o from OrderActivityEntity o where o.status = ?1 and o.performer.id = ?2")
    Page<OrderActivityEntity> findByStatusAndPerformer(Status status, Long id, Pageable pageable);

    @Query("select (count(o) > 0) from OrderActivityEntity o where o.order.id = ?1 and o.status in ?2")
    boolean existsByStatusIn(Long id, Collection<Status> statuses);

    OrderActivityEntity findFirstByOrderIdOrderByTimestampDesc(Long orderId);

    @Query("SELECT oa.order.id, MAX(oa.timestamp), oa.status FROM OrderActivityEntity oa " +
            "WHERE oa.order.id IN :orderIds " +
            "GROUP BY oa.order.id, oa.status")
    Map<Long, Status> findLatestStatusForOrders(@Param("orderIds") List<Long> orderIds);


}