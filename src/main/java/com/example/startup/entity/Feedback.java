package com.example.startup.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {
    @Id
    @GeneratedValue
    private UUID id;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    private JobPost job;

    private int rating;
}
