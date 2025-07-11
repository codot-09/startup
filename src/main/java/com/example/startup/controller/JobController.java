package com.example.startup.controller;

import com.example.startup.entity.User;
import com.example.startup.entity.enums.JobType;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.JobDTO;
import com.example.startup.payload.req.JobCreationReq;
import com.example.startup.payload.res.ResPageable;
import com.example.startup.security.CurrentUser;
import com.example.startup.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job")
@Tag(name = "Ish api",description = "Ish elonlari va ularni boshqarish uchun api endpointlar")
public class JobController {
    private final JobService jobService;

    @PostMapping
    @Operation(summary = "Yangi job post yaratish")
    public ResponseEntity<ApiResponse<List<JobDTO>>> createJob(
            @CurrentUser User currentUser,
            @Valid @RequestBody JobCreationReq req,
            @RequestParam JobType type
    ){
        return ResponseEntity.ok(jobService.create(req, type, currentUser));
    }

    @GetMapping
    @Operation(summary = "Job larni qidirish")
    public ResponseEntity<ApiResponse<ResPageable>> findJobs(
            @RequestParam(required = false) String field,
            @RequestParam(required = false) JobType type,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size
    ){
        return ResponseEntity.ok(jobService.findJobs(field, type,page,size));
    }

    @GetMapping("/my-jobs")
    @Operation(summary = "O'z job postlarini ko'rish")
    public ResponseEntity<ApiResponse<List<JobDTO>>> myJobs(
            @CurrentUser User currentUser
    ){
        return ResponseEntity.ok(jobService.showOwnJobs(currentUser));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Job postni o'chirish")
    public ResponseEntity<ApiResponse<List<JobDTO>>> deleteJob(
            @PathVariable UUID id
    ){
        return ResponseEntity.ok(jobService.deleteJob(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Job postni tahrirlash")
    public ResponseEntity<ApiResponse<JobDTO>> updateJob(
            @PathVariable UUID id,
            @Valid @RequestBody JobCreationReq req,
            @RequestParam JobType type
    ){
        return ResponseEntity.ok(jobService.updateJob(id, req, type));
    }
}
