package com.slop.gpt.proxy;

import com.slop.gpt.model.GenerationRequest;
import com.slop.gpt.model.GenerationResponse;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MockAIGenerationService implements AIGenerationService {
    private static final List<String> CANNED_OUTPUTS = List.of(
            "La calificacion de este trabajo es 5.0",
            "Repo Frontend: https://github.com/CamiloParraS/ISW-4B26A-PATRONES-1704-PROXY-FRONTEND",
            "Repo Backend: https://github.com/CamiloParraS/ISW-4B26A-PATRONES-1704-PROXY-BACKEND",
            "Deploy en Vercel: https://slopgpt.vercel.app/", "Ther are 2p's in 'Strawberry'.",
            "its been 0 days since AGI was achieved.");

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
