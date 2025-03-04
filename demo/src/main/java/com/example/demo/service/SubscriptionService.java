package com.example.demo.service;

import com.example.demo.dto.SubscriptionRequestDto;
import com.example.demo.dto.SubscriptionResponseDto;
import com.example.demo.exception.DuplicateSubscriptionException;
import com.example.demo.exception.InvalidDeletionException;
import com.example.demo.exception.SubscriptionNotFoundException;
import com.example.demo.model.Enums.Status;
import com.example.demo.model.Subscription;
import com.example.demo.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionResponseDto createSubscription(SubscriptionRequestDto requestDto) {
        // Check for duplicate subscriptions
        boolean exists = subscriptionRepository.existsByUserIdAndPlanAndStartDate(
                requestDto.getUserId(), requestDto.getPlan(), requestDto.getStartDate());
        if (exists) {
            throw new DuplicateSubscriptionException("There is already a subscription with the same user, plan, and start date");
        }

        // Map DTO to entity
        Subscription subscription = mapToEntity(requestDto);

        // Save entity to database
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        // Map saved entity to response DTO
        return mapToResponseDto(savedSubscription);
    }

    public void deleteSubscription(UUID id) {
        // Find subscription by ID
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));

        // Check if deletion is allowed based on status
        if (!Set.of(Status.EXPIRED, Status.CANCELLED).contains(subscription.getStatus())) {
            throw new InvalidDeletionException("The subscription is used, it cannot be deleted");
        }

        // Delete subscription
        subscriptionRepository.delete(subscription);
    }

    private Subscription mapToEntity(SubscriptionRequestDto dto) {
        return Subscription.builder()
                .userId(dto.getUserId())
                .plan(dto.getPlan())
                .billingCycle(dto.getBillingCycle())
                .price(dto.getPrice())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(dto.getStatus() != null ? dto.getStatus() : Status.PENDING)
                .build();
    }

    private SubscriptionResponseDto mapToResponseDto(Subscription entity) {
        return new SubscriptionResponseDto(
                entity.getId(),
                entity.getUserId(),
                entity.getPlan(),
                entity.getBillingCycle(),
                entity.getPrice(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getStatus()
        );
    }
}
