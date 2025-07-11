package com.example.startup.service.impl;

import com.example.startup.entity.JobPost;
import com.example.startup.entity.User;
import com.example.startup.entity.enums.JobStatus;
import com.example.startup.entity.enums.JobType;
import com.example.startup.exception.RestException;
import com.example.startup.mapper.JobMapper;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.JobDTO;
import com.example.startup.payload.req.JobCreationReq;
import com.example.startup.payload.res.ResPageable;
import com.example.startup.repository.JobRepository;
import com.example.startup.service.JobService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final JobMapper mapper;

    @Override
    public ApiResponse<List<JobDTO>> create(JobCreationReq req, JobType type, User currentUser) {
        if (jobRepository.findByWorkerId(currentUser.getId()).size() > 4){
            return ApiResponse.error("You have reached the limit",null);
        }else if (jobRepository.existsByType(type)){
            return ApiResponse.error("Job already created",null);
        }
        JobPost newJob = JobPost.builder()
                .title(req.title())
                .description(req.description())
                .worker(currentUser)
                .latitude(req.latitude())
                .longitude(req.longitude())
                .type(type)
                .status(JobStatus.ACTIVE)
                .build();

        jobRepository.save(newJob);

        List<JobPost> posts = jobRepository.findByWorkerId(currentUser.getId());
        return ApiResponse.ok("New job created",mapper.toDtoList(posts));
    }

    @Override
    public ApiResponse<ResPageable> findJobs(String field, JobType type,int page,int size) {
        String typeName = type != null ? type.name() : null;
        Page foundJobs = jobRepository.findJobs(field,typeName,PageRequest.of(page,size));
        List<JobDTO> jobs = null;

        if (!foundJobs.getContent().isEmpty()){
            jobs = mapper.toDtoList(foundJobs.getContent());
        }

        ResPageable response = ResPageable.builder()
                .page(page)
                .size(size)
                .totalPage(foundJobs.getTotalPages())
                .totalElements(foundJobs.getTotalElements())
                .data(jobs)
                .build();

        return ApiResponse.ok(response);
    }

    @Override
    public ApiResponse<List<JobDTO>> showOwnJobs(User currentUser) {
        List<JobPost> foundJobs = jobRepository.findByWorkerId(currentUser.getId());
        List<JobDTO> jobs=  null;

        if (foundJobs.isEmpty()){
            jobs = mapper.toDtoList(foundJobs);
        }

        return ApiResponse.ok(jobs);
    }

    @Override
    public ApiResponse<List<JobDTO>> deleteJob(UUID jobId) {
        JobPost jobPost = jobRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job not found"));

        jobRepository.delete(jobPost);
        return ApiResponse.ok("Job successfully deleted",null);
    }

    @Override
    public ApiResponse<JobDTO> updateJob(UUID jobId, JobCreationReq req,JobType type) {
        JobPost jobPost = jobRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job not found"));

        jobPost.setTitle(req.title());
        jobPost.setDescription(req.description());
        jobPost.setType(type);
        jobPost.setLatitude(req.latitude());
        jobPost.setLongitude(req.longitude());

        jobRepository.save(jobPost);
        return ApiResponse.ok("Job successfully updated",mapper.toDto(jobPost));
    }
}
