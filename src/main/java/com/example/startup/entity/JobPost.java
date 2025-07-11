package com.example.startup.entity;

import com.example.startup.entity.enums.JobStatus;
import com.example.startup.entity.enums.JobType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPost {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    private User worker;

    @Enumerated(EnumType.STRING)
    private JobType type;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private double latitude;

    private double longitude;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Feedback> feedbacks;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Order> orders;
}
