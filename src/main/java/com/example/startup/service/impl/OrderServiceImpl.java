package com.example.startup.service.impl;

import com.example.startup.entity.JobPost;
import com.example.startup.entity.Order;
import com.example.startup.entity.User;
import com.example.startup.entity.enums.JobType;
import com.example.startup.entity.enums.UserRole;
import com.example.startup.exception.RestException;
import com.example.startup.mapper.OrderMapper;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.OrderDTO;
import com.example.startup.payload.req.OrderCreationReq;
import com.example.startup.payload.res.ResPageable;
import com.example.startup.repository.JobRepository;
import com.example.startup.repository.OrderRepository;
import com.example.startup.service.OrderService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final JobRepository jobRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    @Override
    public ApiResponse<ResPageable> createOrder(UUID jobId, User client, OrderCreationReq req) {
        JobPost jobPost = jobRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job not found"));
        if (jobPost.getWorker().isIMBusy()){
            return ApiResponse.error("Worker is busy now",null);
        }

        Order newOrder = Order.builder()
                .customer(client)
                .job(jobPost)
                .orderLat(req.clientLatitude())
                .orderLng(req.clientLongitude())
                .orderDate(req.orderDate())
                .build();

        orderRepository.save(newOrder);

        Page page = orderRepository.findByCustomerId(client.getId(),null,null,null, PageRequest.of(0,10));
        List<OrderDTO> orders = mapper.toDTOList(page.getContent());

        ResPageable response = ResPageable.builder()
                .page(0)
                .size(10)
                .totalPage(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .data(orders)
                .build();
        return ApiResponse.ok("Order successfully created",response);
    }

    @Override
    public ApiResponse<ResPageable> orderHistory(User currentUser, LocalDate startDate, LocalDate endDate, JobType type,int page,int size) {
        Page foundJobs;
        List<OrderDTO> orders = null;
        String typeName = type != null ? type.name() : null;
        if (currentUser.getRole().equals(UserRole.WORKER)) {
            foundJobs = orderRepository.findByWorkerId(currentUser.getId(), startDate, endDate, typeName, PageRequest.of(page, size));
        }
        foundJobs = orderRepository.findByCustomerId(currentUser.getId(), startDate, endDate, typeName, PageRequest.of(page, size));

        if (!foundJobs.getContent().isEmpty()) {
            orders = mapper.toDTOList(foundJobs.getContent());
        }

        ResPageable response = ResPageable.builder()
                .page(page)
                .size(size)
                .totalPage(foundJobs.getTotalPages())
                .totalElements(foundJobs.getTotalElements())
                .data(orders)
                .build();

        return ApiResponse.ok(response);
    }

//    @Override
//    public ApiResponse<List<OrderDTO>> getAll() throws NotFoundException {
//        List<Order> orders = orderRepository.getAll();
//
//        if (orders.isEmpty()) {
//            throw new RestException("Order Not Found",HttpStatus.NOT_FOUND);
//        }
//
//        List<OrderDTO> dtoList = mapper.toDTOList(orders);
//        return ApiResponse.ok(dtoList);
//    }
}
