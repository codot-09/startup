package com.example.startup.service;

import com.example.startup.entity.User;
import com.example.startup.entity.enums.JobType;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.JobDTO;
import com.example.startup.payload.req.JobCreationReq;
import com.example.startup.payload.res.ResPageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface JobService {
    ApiResponse<List<JobDTO>> create(JobCreationReq req, JobType type, User currentUser);
    ApiResponse<ResPageable> findJobs(String field, JobType type,int size,int page);
    ApiResponse<List<JobDTO>> showOwnJobs(User currentUser);
    ApiResponse<List<JobDTO>> deleteJob(UUID jobId);
    ApiResponse<JobDTO> updateJob(UUID jobId,JobCreationReq req,JobType type);
}
