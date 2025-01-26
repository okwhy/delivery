package dev.delivery.services;

import dev.delivery.dtos.ClientDto;
import dev.delivery.dtos.OrderForClientDto;
import dev.delivery.dtos.OrderForClientWithStatusDto;
import dev.delivery.entities.AddressEntity;
import dev.delivery.entities.ClientEntity;
import dev.delivery.entities.OrderEntity;
import dev.delivery.enums.Availability;
import dev.delivery.enums.Status;
import dev.delivery.mappers.AddressMapper;
import dev.delivery.mappers.OrderForClientMapper;
import dev.delivery.mappers.OrderMapper;
import dev.delivery.repos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final AddressRepo addressRepo;
    private final OrderMapper orderMapper;
    private final OrderActivityRepo orderActivityRepo;
    private final ProductRepo productRepo;
    private final ClientRepo clientRepo;
    private final OrderForClientMapper orderForClientMapper;

    public OrderForClientWithStatusDto createOrder(OrderForClientDto order) {
        ClientEntity clientEntity = clientRepo.getReferenceByPhoneNumber(order.getClient().getPhoneNumber());
        if (clientEntity == null) {
            throw new BadCredentialsException("Unauthorized Request");
        }
        OrderEntity orderEntity = orderMapper.toEntity(order);
        orderEntity.setClient(clientEntity);
        orderEntity.getProducts().forEach(product -> {
            if (!(productRepo.existsByIdAndAvailability(product.getProduct().getId(),
                    Availability.IN_STOCK))) {
                throw new IllegalArgumentException("Product " + product.getProduct().getId() + " is out of stock.");
            }
            product.setProduct(productRepo.getReferenceById(product.getProduct().getId()));
        });
        AddressEntity existingAddress = addressRepo.findByLocation(
                orderEntity.getAddress().getLatitude(),
                orderEntity.getAddress().getLongitude());
        if (existingAddress != null) {
            orderEntity.setAddress(existingAddress);
        }
        return orderForClientMapper.toDto(orderRepo.save(orderEntity));
    }

    public List<OrderForClientWithStatusDto> getOrdersByClient(ClientDto client) {
        List<OrderEntity> orders = orderRepo.findByClientPhone(client.getPhoneNumber());
        Map<Long, Status> latestActivities = orderActivityRepo.findLatestStatusForOrders(
                orders.stream().map(OrderEntity::getId).toList());
        return orders.stream()
                .map(order -> {
                    OrderForClientWithStatusDto dto = orderForClientMapper.toDto(order);
                    dto.setStatus(latestActivities.get(order.getId()));
                    return dto;
                })
                .toList();
    }
}
