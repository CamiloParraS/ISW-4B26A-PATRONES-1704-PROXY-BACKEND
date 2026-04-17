package com.slop.gpt.repository;

import com.slop.gpt.model.DailyUsageEntry;
import com.slop.gpt.model.Plan;
import com.slop.gpt.model.QuotaConsumption;
import com.slop.gpt.model.QuotaStatusSnapshot;
import com.slop.gpt.model.RateLimitDecision;
import com.slop.gpt.model.UpgradeHistoryEntry;
import com.slop.gpt.model.UserAccount;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface StateRepository {
    void registerUser(String userId, String email, String username, String encryptedPassword,
            Plan initialPlan, String createdAt);

    UserAccount authenticateUser(String identifier, String encryptedPassword);

    Plan getOrCreatePlan(String userId, LocalDate today);

    RateLimitDecision consumeRateLimit(String userId, Instant now);

    QuotaConsumption consumeQuota(String userId, long tokens, LocalDate today);

    void rollbackQuota(String userId, long tokens, LocalDate today);

    QuotaStatusSnapshot getQuotaStatus(String userId, LocalDate today);

    List<DailyUsageEntry> getLast7DaysUsage(String userId, LocalDate today);

    UpgradeHistoryEntry upgradeFreeToPro(String userId, Instant upgradedAt);

    void resetRateLimits();

    int resetMonthlyQuotas(LocalDate today);
}
