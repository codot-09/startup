package com.example.startup.mapper;

import com.example.startup.entity.Order;
import com.example.startup.payload.OrderDTO;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class OrderMapper {
    public OrderDTO toDTO(Order order){
        return OrderDTO.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .latitude(order.getOrderLat())
                .longitude(order.getOrderLng())
                .workerId(order.getJob().getWorker().getId())
                .jobId(order.getJob().getId())
                .jobType(order.getJob().getType().name())
                .build();
    }

    public List<OrderDTO> toDTOList(List<Order> orders){
        return orders.stream()
                .map(this::toDTO)
                .toList();
    }
}
