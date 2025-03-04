package com.example.demo.repository;

import com.example.demo.model.Enums.Plan;
import com.example.demo.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    boolean existsByUserIdAndPlanAndStartDate(Long userId, Plan plan, LocalDate startDate);
}
