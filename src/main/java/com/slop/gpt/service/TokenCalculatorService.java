package com.slop.gpt.service;

import com.slop.gpt.model.TokenUsageEstimate;
import org.springframework.stereotype.Service;

@Service
public class TokenCalculatorService {
    private static final int DEFAULT_MAX_OUTPUT_TOKENS = 120;

    public TokenUsageEstimate estimate(String prompt, Integer maxOutputTokens) {
        int safeOutputTokens =
                maxOutputTokens == null ? DEFAULT_MAX_OUTPUT_TOKENS : maxOutputTokens;
        int promptLength = prompt == null ? 0 : prompt.trim().length();

        // Deterministic approximation: 1 token ~= 4 characters.
        int promptTokens = Math.max(1, (int) Math.ceil(promptLength / 4.0));
        int totalTokens = promptTokens + safeOutputTokens;

        return new TokenUsageEstimate(promptTokens, safeOutputTokens, totalTokens);
    }
}
