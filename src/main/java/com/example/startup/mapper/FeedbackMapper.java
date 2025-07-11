package com.example.startup.mapper;

import com.example.startup.entity.Feedback;
import com.example.startup.payload.FeedbackDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeedbackMapper {
    public FeedbackDTO toDto(Feedback feedback){
        return FeedbackDTO.builder()
                .id(feedback.getId())
                .message(feedback.getMessage())
                .rating(feedback.getRating())
                .build();
    }

    public List<FeedbackDTO> toDtoList(List<Feedback> feedbacks){
        return feedbacks.stream()
                .map(this::toDto)
                .toList();
    }
}
