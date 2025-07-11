package com.example.startup.repository;

import com.example.startup.entity.JobPost;
import com.example.startup.entity.enums.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<JobPost, UUID> {
    boolean existsByType(JobType type);

    List<JobPost> findByWorkerId(UUID id);

    @Query("""
    SELECT j FROM JobPost j
    WHERE (:field IS NULL OR j.title ILIKE %:field%)
    AND (:type IS NULL OR j.type = :type)
    """)
    Page findJobs(@Param("field")String field,
                  @Param("type")String type,
                  PageRequest request);
}
