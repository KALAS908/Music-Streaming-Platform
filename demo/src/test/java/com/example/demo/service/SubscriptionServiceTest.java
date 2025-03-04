package com.example.demo.service;

import com.example.demo.dto.SubscriptionRequestDto;
import com.example.demo.dto.SubscriptionResponseDto;
import com.example.demo.exception.DuplicateSubscriptionException;
import com.example.demo.exception.InvalidDeletionException;
import com.example.demo.exception.SubscriptionNotFoundException;
import com.example.demo.model.Enums.BillingCycle;
import com.example.demo.model.Enums.Plan;
import com.example.demo.model.Enums.Status;
import com.example.demo.model.Subscription;
import com.example.demo.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    private SubscriptionRequestDto validRequestDto;
    private Subscription validSubscription;
    private UUID validId;
    private UUID invalidID;

    @BeforeEach
    void setUp() {
        validId = UUID.randomUUID();
        invalidID = UUID.randomUUID();

        validRequestDto = new SubscriptionRequestDto(
                1L,
                Plan.PREMIUM,
                BillingCycle.MONTHLY,
                9.99,
                LocalDate.of(2025, 1, 30),
                LocalDate.of(2026, 1, 30),
                null // Status defaults to PENDING
        );

        validSubscription = Subscription.builder()
                .id(validId)
                .userId(1L)
                .plan(Plan.PREMIUM)
                .billingCycle(BillingCycle.MONTHLY)
                .price(9.99)
                .startDate(LocalDate.of(2025, 1, 30))
                .endDate(LocalDate.of(2026, 1, 30))
                .status(Status.PENDING)
                .build();
    }

    @Test
    void shouldCreateSubscriptionSuccessfully() {
        // Arrange
        when(subscriptionRepository.existsByUserIdAndPlanAndStartDate(
                validRequestDto.getUserId(),
                validRequestDto.getPlan(),
                validRequestDto.getStartDate())).thenReturn(false);

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(validSubscription);

        // Act
        SubscriptionResponseDto response = subscriptionService.createSubscription(validRequestDto);

        // Assert
        assertNotNull(response);
        assertEquals(validSubscription.getId(), response.getId());
        assertEquals(validSubscription.getUserId(), response.getUserId());
        assertEquals(validSubscription.getPlan(), response.getPlan());
        assertEquals(Status.PENDING, response.getStatus());
        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    void shouldNotCreateDuplicateSubscription() {
        // Arrange
        when(subscriptionRepository.existsByUserIdAndPlanAndStartDate(
                validRequestDto.getUserId(),
                validRequestDto.getPlan(),
                validRequestDto.getStartDate())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateSubscriptionException.class,
                () -> subscriptionService.createSubscription(validRequestDto));
        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    void shouldDeleteExpiredSubscriptionSuccessfully() {
        // Arrange
        validSubscription.setStatus(Status.EXPIRED);
        when(subscriptionRepository.findById(validId)).thenReturn(Optional.of(validSubscription));

        // Act
        subscriptionService.deleteSubscription(validId);

        // Assert
        verify(subscriptionRepository).delete(validSubscription);
    }

    @Test
    void shouldDeleteCancelledSubscriptionSuccessfully() {
        // Arrange
        validSubscription.setStatus(Status.CANCELLED);
        when(subscriptionRepository.findById(validId)).thenReturn(Optional.of(validSubscription));

        // Act
        subscriptionService.deleteSubscription(validId);

        // Assert
        verify(subscriptionRepository).delete(validSubscription);
    }

    @Test
    void shouldNotDeleteActiveOrPendingSubscriptions() {
        // Test for ACTIVE status
        validSubscription.setStatus(Status.ACTIVE);
        when(subscriptionRepository.findById(validId)).thenReturn(Optional.of(validSubscription));
        assertThrows(InvalidDeletionException.class,
                () -> subscriptionService.deleteSubscription(validId));
        verify(subscriptionRepository, never()).delete(any());

        // Test for PENDING status
        validSubscription.setStatus(Status.PENDING);
        when(subscriptionRepository.findById(validId)).thenReturn(Optional.of(validSubscription));
        assertThrows(InvalidDeletionException.class,
                () -> subscriptionService.deleteSubscription(validId));
        verify(subscriptionRepository, never()).delete(any());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentSubscription() {
        // Arrange
        when(subscriptionRepository.findById(invalidID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SubscriptionNotFoundException.class,
                () -> subscriptionService.deleteSubscription(invalidID));
        verify(subscriptionRepository, never()).delete(any());
    }

    @Test
    void shouldDefaultStatusToPendingIfNotProvided() {
        // Arrange
        when(subscriptionRepository.existsByUserIdAndPlanAndStartDate(any(), any(), any())).thenReturn(false);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(validSubscription);

        // Act
        SubscriptionResponseDto response = subscriptionService.createSubscription(validRequestDto);

        // Assert
        assertEquals(Status.PENDING, response.getStatus());
    }
}
