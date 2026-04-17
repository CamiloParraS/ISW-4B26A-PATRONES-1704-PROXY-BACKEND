package com.slop.gpt.proxy;

import com.slop.gpt.model.GenerationRequest;
import com.slop.gpt.model.GenerationResponse;
import com.slop.gpt.model.QuotaConsumption;
import com.slop.gpt.model.TokenUsageEstimate;
import com.slop.gpt.service.QuotaService;
import com.slop.gpt.service.TokenCalculatorService;

public class QuotaProxyService implements AIGenerationService {
    private final AIGenerationService delegate;
    private final QuotaService quotaService;
    private final TokenCalculatorService tokenCalculatorService;

    public QuotaProxyService(AIGenerationService delegate, QuotaService quotaService,
            TokenCalculatorService tokenCalculatorService) {
        this.delegate = delegate;
        this.quotaService = quotaService;
        this.tokenCalculatorService = tokenCalculatorService;
    }

    @Override
    public GenerationResponse generate(GenerationRequest request) {
        TokenUsageEstimate estimate =
                tokenCalculatorService.estimate(request.getPrompt(), request.getMaxOutputTokens());
        request.setPromptTokens(estimate.getPromptTokens());
        request.setOutputTokensEstimate(estimate.getOutputTokens());
        request.setConsumedTokens(estimate.getTotalTokens());

        QuotaConsumption consumption =
                quotaService.consumeQuota(request.getUserId(), estimate.getTotalTokens());

        try {
            GenerationResponse response = delegate.generate(request);
            response.setCurrentPlan(consumption.getPlan());
            response.setConsumedTokens(estimate.getTotalTokens());
            response.setPromptTokens(estimate.getPromptTokens());
            response.setOutputTokensEstimate(estimate.getOutputTokens());
            response.setMonthlyTokensUsed(consumption.getMonthlyTokensUsed());
            response.setMonthlyTokensRemaining(consumption.getMonthlyTokensRemaining());
            response.setMonthlyResetDate(consumption.getMonthlyResetDate());
            return response;
        } catch (RuntimeException ex) {
            quotaService.rollbackQuota(request.getUserId(), estimate.getTotalTokens());
            throw ex;
        }
    }
}
