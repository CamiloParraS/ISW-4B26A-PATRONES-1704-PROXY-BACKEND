package com.slop.gpt.repository;

import com.slop.gpt.model.AppState;
import com.slop.gpt.model.DailyUsageEntry;
import com.slop.gpt.model.Plan;
import com.slop.gpt.model.QuotaConsumption;
import com.slop.gpt.model.QuotaStatusSnapshot;
import com.slop.gpt.model.RateLimitDecision;
import com.slop.gpt.model.RateLimitWindow;
import com.slop.gpt.model.UpgradeHistoryEntry;
import com.slop.gpt.model.UserAccount;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import tools.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Repository
public class JsonStateRepository implements StateRepository {
    private static final int HISTORY_DAYS = 7;

    private final ObjectMapper objectMapper;
    private final Path persistencePath;
    private final ZoneId zoneId;
    private final ReentrantReadWriteLock lock;

    private AppState state;

    public JsonStateRepository(ObjectMapper objectMapper,
            @Value("${slopgpt.persistence.file:./data/slopgpt-state.json}") String persistenceFile) {
        this.objectMapper = objectMapper;
        this.persistencePath = Path.of(persistenceFile);
        this.zoneId = ZoneId.systemDefault();
        this.lock = new ReentrantReadWriteLock(true);
        this.state = new AppState();
    }

    @PostConstruct
    public void initialize() {
        lock.writeLock().lock();
        try {
            if (persistencePath.getParent() != null) {
                Files.createDirectories(persistencePath.getParent());
            }

            if (!Files.exists(persistencePath) || Files.size(persistencePath) == 0) {
                normalizeStateUnsafe();
                writeStateUnsafe();
                return;
            }

            AppState loadedState = objectMapper.readValue(persistencePath.toFile(), AppState.class);
            if (loadedState != null) {
                state = loadedState;
            }
            normalizeStateUnsafe();
            writeStateUnsafe();
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to initialize JSON persistence file", ex);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void registerUser(String userId, String email, String username, String encryptedPassword,
            Plan initialPlan, String createdAt) {
        lock.writeLock().lock();
        try {
            if (findExistingUserUnsafe(userId, email, username) != null) {
                throw new IllegalStateException("User already exists");
            }

            LocalDate today = LocalDate.now(zoneId);
            UserAccount user = buildDefaultUser(userId, email, username, encryptedPassword,
                    initialPlan, today);
            state.getUsers().put(userId, user);
            writeStateUnsafe();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public UserAccount authenticateUser(String identifier, String password) {
        lock.writeLock().lock();
        try {
            UserAccount user = findUserByIdentifierUnsafe(identifier);
            if (user == null || user.getEncryptedPassword() == null) {
                throw new IllegalStateException("Invalid credentials");
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (!encoder.matches(password, user.getEncryptedPassword())) {
                throw new IllegalStateException("Invalid credentials");
            }

            normalizeUserUnsafe(user, LocalDate.now(zoneId));
            writeStateUnsafe();
            return user;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Plan getOrCreatePlan(String userId, LocalDate today) {
        lock.writeLock().lock();
        try {
            UserAccount user = getOrCreateUserUnsafe(userId, today);
            writeStateUnsafe();
            return user.getPlan();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public RateLimitDecision consumeRateLimit(String userId, Instant now) {
        lock.writeLock().lock();
        try {
            LocalDate today = LocalDate.ofInstant(now, zoneId);
            UserAccount user = getOrCreateUserUnsafe(userId, today);
            Plan plan = user.getPlan();

            if (plan.hasUnlimitedRequests()) {
                writeStateUnsafe();
                return new RateLimitDecision(true, 0, 0, null);
            }

            long epochMinute = now.getEpochSecond() / 60;
            RateLimitWindow window = state.getRateLimitCounters().computeIfAbsent(userId,
                    ignored -> new RateLimitWindow(epochMinute, 0));

            if (window.getEpochMinute() != epochMinute) {
                window.setEpochMinute(epochMinute);
                window.setRequestCount(0);
            }

            if (window.getRequestCount() >= plan.getRequestsPerMinute()) {
                long retryAfter = 60 - (now.getEpochSecond() % 60);
                if (retryAfter <= 0) {
                    retryAfter = 1;
                }
                writeStateUnsafe();
                return new RateLimitDecision(false, retryAfter, window.getRequestCount(), 0);
            }

            int updatedCount = window.getRequestCount() + 1;
            window.setRequestCount(updatedCount);
            int remaining = Math.max(plan.getRequestsPerMinute() - updatedCount, 0);
            writeStateUnsafe();
            return new RateLimitDecision(true, 0, updatedCount, remaining);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public QuotaConsumption consumeQuota(String userId, long tokens, LocalDate today) {
        lock.writeLock().lock();
        try {
            UserAccount user = getOrCreateUserUnsafe(userId, today);
            Plan plan = user.getPlan();

            if (!plan.hasUnlimitedTokens()) {
                long projectedUsage = user.getMonthlyTokensUsed() + tokens;
                if (projectedUsage > plan.getMonthlyTokenQuota()) {
                    Long remaining = calculateRemainingTokens(plan, user.getMonthlyTokensUsed());
                    writeStateUnsafe();
                    return new QuotaConsumption(false, plan, user.getMonthlyTokensUsed(), remaining,
                            user.getMonthlyResetDate());
                }
            }

            user.setMonthlyTokensUsed(user.getMonthlyTokensUsed() + tokens);
            user.getDailyUsage().merge(today.toString(), tokens, Long::sum);
            pruneHistoryUnsafe(user, today);

            writeStateUnsafe();
            return new QuotaConsumption(true, plan, user.getMonthlyTokensUsed(),
                    calculateRemainingTokens(plan, user.getMonthlyTokensUsed()),
                    user.getMonthlyResetDate());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void rollbackQuota(String userId, long tokens, LocalDate today) {
        lock.writeLock().lock();
        try {
            UserAccount user = getOrCreateUserUnsafe(userId, today);

            long updatedMonthlyUsage = Math.max(0L, user.getMonthlyTokensUsed() - tokens);
            user.setMonthlyTokensUsed(updatedMonthlyUsage);

            user.getDailyUsage().compute(today.toString(), (key, value) -> {
                if (value == null) {
                    return null;
                }
                long updated = value - tokens;
                return updated > 0 ? updated : null;
            });

            pruneHistoryUnsafe(user, today);
            writeStateUnsafe();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public QuotaStatusSnapshot getQuotaStatus(String userId, LocalDate today) {
        lock.writeLock().lock();
        try {
            UserAccount user = getOrCreateUserUnsafe(userId, today);
            writeStateUnsafe();
            return new QuotaStatusSnapshot(user.getUserId(), user.getPlan(),
                    user.getMonthlyTokensUsed(),
                    calculateRemainingTokens(user.getPlan(), user.getMonthlyTokensUsed()),
                    user.getMonthlyResetDate());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<DailyUsageEntry> getLast7DaysUsage(String userId, LocalDate today) {
        lock.writeLock().lock();
        try {
            UserAccount user = getOrCreateUserUnsafe(userId, today);
            pruneHistoryUnsafe(user, today);
            writeStateUnsafe();

            List<DailyUsageEntry> entries = new ArrayList<>();
            for (int i = HISTORY_DAYS - 1; i >= 0; i--) {
                LocalDate day = today.minusDays(i);
                long tokensUsed = user.getDailyUsage().getOrDefault(day.toString(), 0L);
                entries.add(new DailyUsageEntry(day.toString(), tokensUsed));
            }
            return entries;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public UpgradeHistoryEntry upgradeFreeToPro(String userId, Instant upgradedAt) {
        lock.writeLock().lock();
        try {
            UserAccount user =
                    getOrCreateUserUnsafe(userId, LocalDate.ofInstant(upgradedAt, zoneId));
            if (user.getPlan() != Plan.FREE) {
                throw new IllegalStateException(
                        "Only FREE users can be upgraded with this endpoint");
            }

            UpgradeHistoryEntry upgradeEntry =
                    new UpgradeHistoryEntry(userId, Plan.FREE, Plan.PRO, upgradedAt.toString());

            user.setPlan(Plan.PRO);
            state.getUpgradeHistory().add(upgradeEntry);
            writeStateUnsafe();
            return upgradeEntry;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void resetRateLimits() {
        lock.writeLock().lock();
        try {
            state.getRateLimitCounters().clear();
            writeStateUnsafe();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int resetMonthlyQuotas(LocalDate today) {
        lock.writeLock().lock();
        try {
            int resetCount = 0;
            String currentMonth = YearMonth.from(today).toString();

            for (UserAccount user : state.getUsers().values()) {
                if (!currentMonth.equals(user.getCurrentMonth())) {
                    user.setCurrentMonth(currentMonth);
                    user.setMonthlyTokensUsed(0L);
                    user.setMonthlyResetDate(nextMonthlyResetDate(today));
                    resetCount++;
                }
                pruneHistoryUnsafe(user, today);
            }

            if (resetCount > 0) {
                writeStateUnsafe();
            }
            return resetCount;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void normalizeStateUnsafe() {
        if (state == null) {
            state = new AppState();
        }
        if (state.getUsers() == null) {
            state.setUsers(new HashMap<>());
        }
        if (state.getRateLimitCounters() == null) {
            state.setRateLimitCounters(new HashMap<>());
        }
        if (state.getUpgradeHistory() == null) {
            state.setUpgradeHistory(new ArrayList<>());
        }

        LocalDate today = LocalDate.now(zoneId);
        for (UserAccount user : state.getUsers().values()) {
            normalizeUserUnsafe(user, today);
        }
    }

    private UserAccount getOrCreateUserUnsafe(String userId, LocalDate today) {
        UserAccount existing = state.getUsers().get(userId);
        if (existing == null) {
            UserAccount created = buildDefaultUser(userId, null, null, null, Plan.FREE, today);
            state.getUsers().put(userId, created);
            return created;
        }

        normalizeUserUnsafe(existing, today);
        return existing;
    }

    private UserAccount buildDefaultUser(String userId, String email, String username,
            String encryptedPassword, Plan plan, LocalDate today) {
        return new UserAccount(userId, email, username, encryptedPassword, plan,
                YearMonth.from(today).toString(), nextMonthlyResetDate(today));
    }

    private void normalizeUserUnsafe(UserAccount user, LocalDate today) {
        if (user.getDailyUsage() == null) {
            user.setDailyUsage(new HashMap<>());
        }
        if (user.getPlan() == null) {
            user.setPlan(Plan.FREE);
        }
        if (user.getCurrentMonth() == null || user.getCurrentMonth().isBlank()) {
            user.setCurrentMonth(YearMonth.from(today).toString());
        }
        if (user.getMonthlyResetDate() == null || user.getMonthlyResetDate().isBlank()) {
            user.setMonthlyResetDate(nextMonthlyResetDate(today));
        }

        String currentMonth = YearMonth.from(today).toString();
        if (!currentMonth.equals(user.getCurrentMonth())) {
            user.setCurrentMonth(currentMonth);
            user.setMonthlyTokensUsed(0L);
            user.setMonthlyResetDate(nextMonthlyResetDate(today));
        }

        pruneHistoryUnsafe(user, today);
    }

    private UserAccount findExistingUserUnsafe(String userId, String email, String username) {
        if (state.getUsers().containsKey(userId)) {
            return state.getUsers().get(userId);
        }

        for (UserAccount user : state.getUsers().values()) {
            if (email != null && user.getEmail() != null
                    && user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
            if (username != null && user.getUsername() != null
                    && user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    private UserAccount findUserByIdentifierUnsafe(String identifier) {
        UserAccount directMatch = state.getUsers().get(identifier);
        if (directMatch != null) {
            return directMatch;
        }

        for (UserAccount user : state.getUsers().values()) {
            if (user.getEmail() != null && user.getEmail().equalsIgnoreCase(identifier)) {
                return user;
            }
            if (user.getUsername() != null && user.getUsername().equalsIgnoreCase(identifier)) {
                return user;
            }
        }
        return null;
    }

    private void pruneHistoryUnsafe(UserAccount user, LocalDate today) {
        LocalDate threshold = today.minusDays(HISTORY_DAYS - 1L);
        Iterator<Map.Entry<String, Long>> iterator = user.getDailyUsage().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            try {
                LocalDate usageDate = LocalDate.parse(entry.getKey());
                if (usageDate.isBefore(threshold)) {
                    iterator.remove();
                }
            } catch (DateTimeParseException ignored) {
                iterator.remove();
            }
        }
    }

    private Long calculateRemainingTokens(Plan plan, long usedTokens) {
        if (plan.hasUnlimitedTokens()) {
            return null;
        }
        long remaining = plan.getMonthlyTokenQuota() - usedTokens;
        return Math.max(remaining, 0L);
    }

    private String nextMonthlyResetDate(LocalDate today) {
        return YearMonth.from(today).plusMonths(1).atDay(1).toString();
    }

    private void writeStateUnsafe() {
        try {
            Path tempPath = persistencePath.resolveSibling(persistencePath.getFileName() + ".tmp");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(tempPath.toFile(), state);
            Files.move(tempPath, persistencePath, StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to persist JSON state", ex);
        }
    }
}
