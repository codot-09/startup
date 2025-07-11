package com.example.startup.mapper;

import com.example.startup.entity.JobPost;
import com.example.startup.payload.JobDTO;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class JobMapper {
    public JobDTO toDto(JobPost jobPost){
        return JobDTO.builder()
                .id(jobPost.getId())
                .title(jobPost.getTitle())
                .description(jobPost.getDescription())
                .jobType(jobPost.getType().name())
                .jobStatus(jobPost.getStatus().name())
                .workerName(jobPost.getWorker().getName())
                .workerPhone(jobPost.getWorker().getPhone())
                .latitude(jobPost.getLatitude())
                .longitude(jobPost.getLongitude())
                .build();
    }

    public List<JobDTO> toDtoList(List<JobPost> jobs){
        return jobs.stream()
                .map(this::toDto)
                .toList();
    }
}
