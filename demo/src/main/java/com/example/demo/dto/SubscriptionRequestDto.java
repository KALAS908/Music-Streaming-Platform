package com.example.demo.dto;

import com.example.demo.model.Enums.BillingCycle;
import com.example.demo.model.Enums.Plan;
import com.example.demo.model.Enums.Status;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequestDto {

    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be a positive value")
    private Long userId;

    @NotNull(message = "Plan cannot be null")
    private Plan plan;

    @NotNull(message = "Billing cycle cannot be null")
    private BillingCycle billingCycle;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be a positive value")
    private Double price;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDate startDate;

    @Future(message = "End date must be in the future if provided")
    private LocalDate endDate;

    private Status status; // Optional, defaults to PENDING if not provided
}


