package com.slop.gpt.controller;

import com.slop.gpt.dto.DailyUsageDto;
import com.slop.gpt.dto.QuotaHistoryResponseDto;
import com.slop.gpt.dto.QuotaStatusResponseDto;
import com.slop.gpt.dto.UpgradePlanRequestDto;
import com.slop.gpt.dto.UpgradePlanResponseDto;
import com.slop.gpt.model.DailyUsageEntry;
import com.slop.gpt.model.QuotaStatusSnapshot;
import com.slop.gpt.model.UpgradeHistoryEntry;
import com.slop.gpt.service.QuotaService;
import com.slop.gpt.service.SubscriptionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/quota")
public class QuotaController {
    private final QuotaService quotaService;
    private final SubscriptionService subscriptionService;

    public QuotaController(QuotaService quotaService, SubscriptionService subscriptionService) {
        this.quotaService = quotaService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/status")
    public ResponseEntity<QuotaStatusResponseDto> getQuotaStatus(
            @RequestParam @NotBlank(message = "userId is required") String userId) {
        QuotaStatusSnapshot snapshot = quotaService.getQuotaStatus(userId);

        QuotaStatusResponseDto response = new QuotaStatusResponseDto();
        response.setUserId(snapshot.getUserId());
        response.setCurrentPlan(snapshot.getPlan());
        response.setMonthlyTokensUsed(snapshot.getMonthlyTokensUsed());
        response.setMonthlyTokensRemaining(snapshot.getMonthlyTokensRemaining());
        response.setMonthlyResetDate(snapshot.getMonthlyResetDate());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<QuotaHistoryResponseDto> getQuotaHistory(
            @RequestParam @NotBlank(message = "userId is required") String userId) {
        List<DailyUsageEntry> usageEntries = quotaService.getLast7DaysUsage(userId);

        QuotaHistoryResponseDto response = new QuotaHistoryResponseDto();
        response.setUserId(userId);
        response.setLast7Days(usageEntries.stream()
                .map(entry -> new DailyUsageDto(entry.getDate(), entry.getTokensUsed())).toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/upgrade")
    public ResponseEntity<UpgradePlanResponseDto> upgradePlan(
            @Valid @RequestBody UpgradePlanRequestDto request) {
        UpgradeHistoryEntry entry = subscriptionService.upgradeFreeToPro(request.getUserId());

        UpgradePlanResponseDto response = new UpgradePlanResponseDto();
        response.setUserId(entry.getUserId());
        response.setFromPlan(entry.getFromPlan());
        response.setToPlan(entry.getToPlan());
        response.setUpgradedAt(entry.getUpgradedAt());

        return ResponseEntity.ok(response);
    }
}
