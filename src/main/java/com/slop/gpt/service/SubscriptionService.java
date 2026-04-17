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

@Service
public class SubscriptionService {
    private final StateRepository stateRepository;
    private final Clock clock;

    public SubscriptionService(StateRepository stateRepository, Clock clock) {
        this.stateRepository = stateRepository;
        this.clock = clock;
    }

    public void registerUser(String userId, String email, String username,
            String encryptedPassword) {
        try {
            stateRepository.registerUser(userId, email, username, encryptedPassword, Plan.FREE,
                    Instant.now(clock).toString());
        } catch (IllegalStateException ex) {
            throw new ResourceConflictException("User already exists");
        }
    }

    public UserAccount loginUser(String identifier, String encryptedPassword) {
        try {
            return stateRepository.authenticateUser(identifier, encryptedPassword);
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
