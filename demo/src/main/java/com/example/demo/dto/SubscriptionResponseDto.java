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
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDto {

    private UUID id;

    private Long userId;

    private Plan plan;

    private BillingCycle billingCycle;

    private Double price;

    private LocalDate startDate;

    private LocalDate endDate;

    private Status status;
}
