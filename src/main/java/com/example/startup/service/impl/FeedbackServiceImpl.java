package com.example.startup.service.impl;

import com.example.startup.bot_service.RegisterBot;
import com.example.startup.entity.Feedback;
import com.example.startup.entity.JobPost;
import com.example.startup.exception.ResourceNotFoundException;
import com.example.startup.mapper.FeedbackMapper;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.FeedbackDTO;
import com.example.startup.repository.FeedbackRepository;
import com.example.startup.repository.JobRepository;
import com.example.startup.service.FeedbackService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final RegisterBot bot;
    private final FeedbackRepository feedbackRepository;
    private final JobRepository jobRepository;
    private final FeedbackMapper mapper;

    @Override
    public ApiResponse<FeedbackDTO> create(FeedbackDTO request, UUID postId) {
        JobPost job = jobRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("E'lon topilmadi"));

        Feedback newFeedback = feedbackRepository.save(Feedback.builder()
                .message(request.message())
                .rating(request.rating())
                .job(job)
                .build());

        bot.sendMessageToChat(job.getWorker().getChatId(),"Sizga izoh bildirishdi");
        return ApiResponse.ok("Izoh qoldirildi",null);
    }

    @Override
    public ApiResponse<List<FeedbackDTO>> getFeedback(UUID postId) {
        List<Feedback> feedbacks = feedbackRepository.findByJobId(postId);
        return ApiResponse.ok(mapper.toDtoList(feedbacks));
    }
}
