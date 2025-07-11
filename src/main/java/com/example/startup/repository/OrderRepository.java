package com.example.startup.repository;

import com.example.startup.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("""
    SELECT o FROM Order o
    WHERE o.customer.id = :id
      AND (:date1 IS NULL OR o.orderDate >= :date1)
      AND (:date2 IS NULL OR o.orderDate <= :date2)
      AND (:type IS NULL OR o.job.type = :type)
""")
    Page findByCustomerId(
            @Param("id") UUID id,
            @Param("date1")LocalDate startDate,
            @Param("date2")LocalDate endDate,
            @Param("type")String type,
            PageRequest pageRequest
            );

    @Query("""
    SELECT o FROM Order o
    WHERE o.job.worker.id = :id
      AND (:date1 IS NULL OR o.orderDate >= :date1)
      AND (:date2 IS NULL OR o.orderDate <= :date2)
      AND (:type IS NULL OR o.job.type = :type)
""")
    Page findByWorkerId(
            @Param("id") UUID id,
            @Param("date1")LocalDate startDate,
            @Param("date2")LocalDate endDate,
            @Param("type")String type,
            PageRequest pageRequest
    );
    @Query("""
select o from Order o
""")
    List<Order> getAll();
}
