package com.example.demo.controller;

import com.example.demo.dto.SubscriptionRequestDto;
import com.example.demo.dto.SubscriptionResponseDto;
import com.example.demo.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
@AllArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponseDto> createSubscription(
            @Valid @RequestBody SubscriptionRequestDto requestDto) {
        // Delegate to service and return the response DTO
        SubscriptionResponseDto responseDto = subscriptionService.createSubscription(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable UUID id) {
        // Delegate deletion logic to service
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}
