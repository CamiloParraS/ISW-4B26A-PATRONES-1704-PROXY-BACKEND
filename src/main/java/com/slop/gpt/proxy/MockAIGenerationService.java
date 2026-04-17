package com.slop.gpt.proxy;

import com.slop.gpt.model.GenerationRequest;
import com.slop.gpt.model.GenerationResponse;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MockAIGenerationService implements AIGenerationService {
    private static final List<String> CANNED_OUTPUTS = List.of(
            "Here is a concise plan with practical next steps and measurable outcomes.",
            "The idea can be improved by reducing scope, validating assumptions, and iterating quickly.",
            "A robust implementation should separate orchestration, persistence, and policy decisions.",
            "Try starting with a minimal version, then add observability and guardrails incrementally.",
            "The proposal is viable if you keep the API contract stable and measure token consumption.");

    @Override
    public GenerationResponse generate(GenerationRequest request) {
        long startedAt = System.currentTimeMillis();
        try {
            Thread.sleep(1200L);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Mock generation was interrupted", ex);
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(CANNED_OUTPUTS.size());
        String responseText = CANNED_OUTPUTS.get(randomIndex) + " Prompt fragment: \""
                + trimPrompt(request.getPrompt()) + "\".";

        GenerationResponse response = new GenerationResponse();
        response.setUserId(request.getUserId());
        response.setGeneratedText(responseText);
        response.setProcessingTimeMs(System.currentTimeMillis() - startedAt);
        return response;
    }

    private String trimPrompt(String prompt) {
        if (prompt == null) {
            return "";
        }
        String normalized = prompt.trim();
        if (normalized.length() <= 60) {
            return normalized;
        }
        return normalized.substring(0, 60) + "...";
    }
}
