package dev.delivery.services;

import dev.delivery.dtos.OrderActivityCreateDto;
import dev.delivery.dtos.OrderActivityDto;
import dev.delivery.dtos.OrderForClientWithStatusDto;
import dev.delivery.entities.OrderActivityEntity;
import dev.delivery.entities.OrderEntity;
import dev.delivery.enums.Role;
import dev.delivery.enums.Status;
import dev.delivery.mappers.OrderActivityCreateMapper;
import dev.delivery.mappers.OrderActivityMapper;
import dev.delivery.mappers.OrderForClientMapper;
import dev.delivery.repos.OrderActivityRepo;
import dev.delivery.repos.OrderRepo;
import dev.delivery.repos.PerformerRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SqlFragmentAlias;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderActivityService {
    private final OrderActivityRepo orderActivityRepo;
    private final OrderRepo orderRepo;
    private final OrderActivityCreateMapper activityCreateMapper;
    private final OrderActivityMapper activityMapper;
    private final PerformerRepo performerRepo;
    //todo в конфиг
    private final Map<Role, Set<Status>> ROLE_STATUS_MAP = Map.of(
            Role.MANAGER, Set.of(Status.CREATED, Status.CANCELED, Status.PENDING_PACKED, Status.PACKED, Status.PENDING_DELIVERY, Status.DELIVERED, Status.COMPLETED),
            Role.PACKER, Set.of(Status.PENDING_PACKED, Status.PACKED),
            Role.COURIER, Set.of(Status.PENDING_DELIVERY, Status.DELIVERED)
    );
    private final Map<Role, Set<Status>> STATUSES_ONLY_PERFORMER = Map.of(
            Role.PACKER, Set.of(Status.PACKED),
            Role.COURIER, Set.of(Status.DELIVERED)
    );
    private static final Map<Status, Set<Status>> STATUS_TRANSITIONS = Map.ofEntries(
            entry(Status.CREATED, Set.of(Status.PENDING_PACKED, Status.CANCELED)),
            entry(Status.PENDING_PACKED, Set.of(Status.PACKED, Status.CANCELED)),
            entry(Status.PACKED, Set.of(Status.PENDING_DELIVERY, Status.CANCELED)),
            entry(Status.PENDING_DELIVERY, Set.of(Status.DELIVERED, Status.CANCELED)),
            entry(Status.DELIVERED, Set.of(Status.COMPLETED, Status.CANCELED)),
            entry(Status.COMPLETED, Set.of()),
            entry(Status.CANCELED, Set.of())
    );
    private final OrderForClientMapper orderForClientMapper;


    public OrderActivityDto publishActivity(OrderActivityCreateDto data, Long userId, List<SimpleGrantedAuthority> authorities) {
        Role userRole = extractUserRole(authorities);
        if (ROLE_STATUS_MAP.get(userRole).contains(data.getStatus())) {
            return createActivity(userId, data);
        }
        throw new IllegalArgumentException("Cannot publish activity with status " + data.getStatus() + " by role " + userRole);
    }


    public Page<OrderActivityDto> getActivities(List<Status> statuses, List<SimpleGrantedAuthority> authorities, Long id, Pageable pageable) {
        Role userRole = validateRole(statuses, authorities);
        return fetchActivities(statuses, userRole, id, pageable);
    }

    public Page<OrderForClientWithStatusDto> getOrdersByStatus(List<Status> statuses, List<SimpleGrantedAuthority> authorities, Long id, Pageable pageable) {
        validateRole(statuses, authorities);
        return fetchOrders(statuses, pageable);
    }

    private Role extractUserRole(List<SimpleGrantedAuthority> authorities) {
        return Role.valueOf(authorities.stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .filter(role -> {
                    try {
                        Role.valueOf(role);
                        return true;
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                })
                .findFirst()
                .orElseThrow(() -> new BadCredentialsException("Token has invalid structure")));
    }

    private Role validateRole(List<Status> statuses, List<SimpleGrantedAuthority> authorities) {
        Role userRole = extractUserRole(authorities);
        if (statuses == null || statuses.isEmpty()) {
            if (userRole != Role.MANAGER)
                throw new IllegalArgumentException("Cannot fetch by role " + userRole);
            else return userRole;
        }
        for (Status status : statuses) {
            if (!ROLE_STATUS_MAP.get(userRole).contains(status)) {
                throw new IllegalArgumentException("Status " + status + " is not allowed for role " + userRole);
            }
        }
        return userRole;
    }

    private Page<OrderForClientWithStatusDto> fetchOrders(List<Status> statuses, Pageable pageable) {
        log.info("statuses {}", statuses);
        if (statuses == null || statuses.isEmpty()) {
            return orderRepo.findWithoutStatus(pageable).map(orderForClientMapper::toDto);
        }
        Page<OrderEntity> orders = orderRepo.findByPresentStatuses(statuses, pageable);
        return orders.map(orderForClientMapper::toDto);
    }

    private Page<OrderActivityDto> fetchActivities(List<Status> statuses, Role userRole, Long userId, Pageable pageable) {
        if (!STATUSES_ONLY_PERFORMER.containsKey(userRole)) {
            Page<OrderActivityEntity> entities = orderActivityRepo.findByStatusIn(statuses, pageable);
            return entities.map(activityMapper::toDto);
        }
        List<OrderActivityEntity> entities = new ArrayList<>();
        for (Status status : statuses) {
            if (STATUSES_ONLY_PERFORMER.get(userRole).contains(status)) {
                entities.addAll(orderActivityRepo.findByStatusAndPerformer(status, userId, pageable).getContent());
            } else {
                entities.addAll(orderActivityRepo.findByStatus(status, pageable).getContent());
            }
        }
        return new PageImpl<>(entities.stream().map(activityMapper::toDto).toList(), pageable, orderActivityRepo.count());
    }

    private OrderActivityDto createActivity(Long userid, OrderActivityCreateDto data) {
        OrderActivityEntity latest = orderActivityRepo.findFirstByOrderIdOrderByTimestampDesc(data.getOrder());
        if (!isValidStatusTransition(latest != null ? latest.getStatus() : null
                , data.getStatus())) {
            throw new IllegalArgumentException("Attempted to create activity with invalid status " + data.getStatus());
        }
        OrderActivityEntity activity = activityCreateMapper.toEntity(data);
        activity.setOrder(orderRepo.findById(data.getOrder()).orElseThrow());
        activity.setTimestamp(LocalDateTime.now());
        activity.setPerformer(performerRepo.getReferenceById(userid));
        return activityMapper.toDto(orderActivityRepo.save(activity));
    }

    private boolean isValidStatusTransition(Status currentStatus, Status newStatus) {
        if (newStatus == Status.CANCELED) {
            return currentStatus != Status.COMPLETED;
        }
        if (currentStatus == null) {
            return newStatus == Status.CREATED;
        }
        Set<Status> allowedTransitions = STATUS_TRANSITIONS.get(currentStatus);
        return allowedTransitions != null && allowedTransitions.contains(newStatus);
    }
}
