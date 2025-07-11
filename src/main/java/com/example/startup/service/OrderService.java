package com.example.startup.service;

import com.example.startup.entity.User;
import com.example.startup.entity.enums.JobType;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.OrderDTO;
import com.example.startup.payload.req.OrderCreationReq;
import com.example.startup.payload.res.ResPageable;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public interface OrderService {
    ApiResponse<ResPageable> createOrder(UUID jobId, User client, OrderCreationReq req);
    ApiResponse<ResPageable> orderHistory(User currentUser, LocalDate startDate, LocalDate endDate, JobType type,int page,int size);
//    ApiResponse<List<OrderDTO>> getAll() throws NotFoundException;
}
