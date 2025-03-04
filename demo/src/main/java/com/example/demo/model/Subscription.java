package com.example.demo.model;

import com.example.demo.model.Enums.BillingCycle;
import com.example.demo.model.Enums.Plan;
import com.example.demo.model.Enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "subscriptions",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "plan", "startDate"})
})
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull @Positive
    private Long userId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Plan plan;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BillingCycle billingCycle;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Positive
    private double price;

    @NotNull
    private LocalDate startDate;
    private LocalDate endDate;

}




