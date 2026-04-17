package com.slop.gpt.service;

import com.slop.gpt.exception.QuotaExceededException;
import com.slop.gpt.model.DailyUsageEntry;
import com.slop.gpt.model.QuotaConsumption;
import com.slop.gpt.model.QuotaStatusSnapshot;
import com.slop.gpt.repository.StateRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
public class QuotaService {
    private final StateRepository stateRepository;
    private final Clock clock;

    public QuotaService(StateRepository stateRepository, Clock clock) {
        this.stateRepository = stateRepository;
        this.clock = clock;
    }

    public QuotaConsumption consumeQuota(String userId, long tokens) {
        LocalDate today = LocalDate.now(clock);
        QuotaConsumption consumption = stateRepository.consumeQuota(userId, tokens, today);

        if (!consumption.isAllowed()) {
            throw new QuotaExceededException(
                    "Monthly token quota exhausted for your current plan. Upgrade your plan or wait until "
                            + consumption.getMonthlyResetDate() + ".");
        }

        return consumption;
    }

    public void rollbackQuota(String userId, long tokens) {
        stateRepository.rollbackQuota(userId, tokens, LocalDate.now(clock));
    }

    public QuotaStatusSnapshot getQuotaStatus(String userId) {
        return stateRepository.getQuotaStatus(userId, LocalDate.now(clock));
    }

    public List<DailyUsageEntry> getLast7DaysUsage(String userId) {
        return stateRepository.getLast7DaysUsage(userId, LocalDate.now(clock));
    }
}
