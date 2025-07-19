package com.example.startup.controller;

import com.example.startup.entity.User;
import com.example.startup.entity.enums.JobType;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.OrderDTO;
import com.example.startup.payload.req.OrderCreationReq;
import com.example.startup.payload.res.ResPageable;
import com.example.startup.security.CurrentUser;
import com.example.startup.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
@Tag(name = "Buyurtma api",description = "Buyurtma va ularni boshqarish uchun api endpointlar")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/{jobId}")
    @Operation(summary = "Yangi order yaratish")
    public ResponseEntity<ApiResponse<String>> createOrder(
            @RequestBody OrderCreationReq req,
            @CurrentUser User user,
            @PathVariable UUID jobId
    ){
        return ResponseEntity.ok(orderService.createOrder(jobId,user,req));
    }

    @GetMapping
    @Operation(summary = "Orderlar tarixini ko'rish")
    public ResponseEntity<ApiResponse<ResPageable>> showOrders(
            @CurrentUser User user,
            @RequestParam(required = false)LocalDate startDate,
            @RequestParam(required = false)LocalDate endDate,
            @RequestParam(required = false)JobType type,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size
    ){
        return ResponseEntity.ok(orderService.orderHistory(user,startDate,endDate,type,page,size));
    }

//    @GetMapping("/all")
//    @Operation(summary = "hammma orderlarni ko'rish")
//    public ResponseEntity<ApiResponse<List<OrderDTO>>> findAll() throws NotFoundException {
//        return ResponseEntity.ok(orderService.getAll());
//    }
}
