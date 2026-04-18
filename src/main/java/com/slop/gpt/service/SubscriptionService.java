package com.slop.gpt.service;

import com.slop.gpt.exception.BadRequestException;
import com.slop.gpt.exception.ResourceConflictException;
import com.slop.gpt.model.Plan;
import com.slop.gpt.model.UpgradeHistoryEntry;
import com.slop.gpt.model.UserAccount;
import com.slop.gpt.repository.StateRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class SubscriptionService {
    private final StateRepository stateRepository;
    private final Clock clock;

    public SubscriptionService(StateRepository stateRepository, Clock clock) {
        this.stateRepository = stateRepository;
        this.clock = clock;
    }

    public String registerUser(String email, String username, String password) {
        try {
            String userId = UUID.randomUUID().toString();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashed = encoder.encode(password);
            stateRepository.registerUser(userId, email, username, hashed, Plan.FREE,
                    Instant.now(clock).toString());
            return userId;
        } catch (IllegalStateException ex) {
            throw new ResourceConflictException("User already exists");
        }
    }

    public UserAccount loginUser(String identifier, String password) {
        try {
            return stateRepository.authenticateUser(identifier, password);
        } catch (IllegalStateException ex) {
            throw new BadRequestException("Invalid credentials");
        }
    }

    public UpgradeHistoryEntry upgradeFreeToPro(String userId) {
        try {
            return stateRepository.upgradeFreeToPro(userId, Instant.now(clock));
        } catch (IllegalStateException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }
}
