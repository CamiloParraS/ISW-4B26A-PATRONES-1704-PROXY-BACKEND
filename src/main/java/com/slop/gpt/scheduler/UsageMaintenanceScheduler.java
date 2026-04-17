package com.slop.gpt.scheduler;

import com.slop.gpt.repository.StateRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;

@Component
public class UsageMaintenanceScheduler {
    private final StateRepository stateRepository;
    private final Clock clock;

    public UsageMaintenanceScheduler(StateRepository stateRepository, Clock clock) {
        this.stateRepository = stateRepository;
        this.clock = clock;
    }

    @Scheduled(cron = "0 * * * * *")
    public void resetRateLimitsEveryMinute() {
        stateRepository.resetRateLimits();
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void resetMonthlyQuotas() {
        stateRepository.resetMonthlyQuotas(LocalDate.now(clock));
    }
}
