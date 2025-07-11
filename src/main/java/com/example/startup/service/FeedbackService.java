package com.example.startup.service;

import com.example.startup.entity.Feedback;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.FeedbackDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface FeedbackService {
    ApiResponse<FeedbackDTO> create(FeedbackDTO request, UUID postId);
    ApiResponse<List<FeedbackDTO>> getFeedback(UUID postId);
}
