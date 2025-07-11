package com.example.startup.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private JobPost job;

    @ManyToOne(fetch = FetchType.LAZY)
    private User customer;

    @Column(nullable = false)
    private LocalDate orderDate;

    private double orderLat;

    private double orderLng;
}
