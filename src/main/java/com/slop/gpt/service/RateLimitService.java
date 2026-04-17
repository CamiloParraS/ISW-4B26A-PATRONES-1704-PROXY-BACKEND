package com.slop.gpt.service;

import com.slop.gpt.exception.RateLimitExceededException;
import com.slop.gpt.model.RateLimitDecision;
import com.slop.gpt.repository.StateRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
public class RateLimitService {
    private final StateRepository stateRepository;
    private final Clock clock;

    public RateLimitService(StateRepository stateRepository, Clock clock) {
        this.stateRepository = stateRepository;
        this.clock = clock;
    }

    public RateLimitDecision consumeRequestSlot(String userId) {
        RateLimitDecision decision = stateRepository.consumeRateLimit(userId, Instant.now(clock));
        if (!decision.isAllowed()) {
            throw new RateLimitExceededException(
                    "You have reached the request limit for your current plan. Please retry shortly.",
                    decision.getRetryAfterSeconds());
        }
        return decision;
    }
}
