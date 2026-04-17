package com.slop.gpt.service;

import com.slop.gpt.model.GenerationRequest;
import com.slop.gpt.model.GenerationResponse;
import com.slop.gpt.proxy.AIGenerationService;
import org.springframework.stereotype.Service;

@Service
public class GenerationOrchestratorService {
    private final AIGenerationService aiGenerationService;

    public GenerationOrchestratorService(AIGenerationService aiGenerationService) {
        this.aiGenerationService = aiGenerationService;
    }

    public GenerationResponse generate(GenerationRequest request) {
        return aiGenerationService.generate(request);
    }
}
