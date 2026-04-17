package com.slop.gpt.controller;

import com.slop.gpt.dto.UserRegistrationRequestDto;
import com.slop.gpt.dto.UserRegistrationResponseDto;
import com.slop.gpt.model.Plan;
import com.slop.gpt.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final SubscriptionService subscriptionService;

    public UserController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserRegistrationResponseDto> register(
            @Valid @RequestBody UserRegistrationRequestDto request) {
        // Password arrives already encrypted and is persisted as-is by design.
        subscriptionService.registerUser(request.getUserId(), request.getEncryptedPassword());

        UserRegistrationResponseDto response = new UserRegistrationResponseDto();
        response.setUserId(request.getUserId());
        response.setCurrentPlan(Plan.FREE);
        response.setCreatedAt(Instant.now().toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
