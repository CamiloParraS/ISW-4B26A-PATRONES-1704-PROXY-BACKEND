package com.slop.gpt.controller;

import com.slop.gpt.dto.UserRegistrationRequestDto;
import com.slop.gpt.dto.UserRegistrationResponseDto;
import com.slop.gpt.dto.UserLoginRequestDto;
import com.slop.gpt.dto.UserLoginResponseDto;
import com.slop.gpt.model.Plan;
import com.slop.gpt.model.UserAccount;
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
import com.slop.gpt.util.JwtUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final SubscriptionService subscriptionService;
    private final JwtUtil jwtUtil;

    public UserController(SubscriptionService subscriptionService, JwtUtil jwtUtil) {
        this.subscriptionService = subscriptionService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserRegistrationResponseDto> register(
            @Valid @RequestBody UserRegistrationRequestDto request) {
        String userId = subscriptionService.registerUser(request.getEmail(), request.getUsername(),
                request.getPassword());

        UserRegistrationResponseDto response = new UserRegistrationResponseDto();
        response.setUserId(userId);
        response.setEmail(request.getEmail());
        response.setUsername(request.getUsername());
        response.setCurrentPlan(Plan.FREE);
        response.setCreatedAt(Instant.now().toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(
            @Valid @RequestBody UserLoginRequestDto request) {
        UserAccount account =
                subscriptionService.loginUser(request.getIdentifier(), request.getPassword());

        UserLoginResponseDto response = new UserLoginResponseDto();
        response.setUserId(account.getUserId());
        response.setEmail(account.getEmail());
        response.setUsername(account.getUsername());
        response.setCurrentPlan(account.getPlan());
        response.setLoggedInAt(Instant.now().toString());
        String token = jwtUtil.generateToken(account.getUserId());
        response.setToken(token);

        return ResponseEntity.ok(response);
    }
}
