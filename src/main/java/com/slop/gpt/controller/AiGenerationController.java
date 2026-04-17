package com.slop.gpt.controller;

import com.slop.gpt.dto.GenerationRequestDto;
import com.slop.gpt.dto.GenerationResponseDto;
import com.slop.gpt.model.GenerationRequest;
import com.slop.gpt.model.GenerationResponse;
import com.slop.gpt.service.GenerationOrchestratorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiGenerationController {
    private final GenerationOrchestratorService generationOrchestratorService;

    public AiGenerationController(GenerationOrchestratorService generationOrchestratorService) {
        this.generationOrchestratorService = generationOrchestratorService;
    }

    @PostMapping("/generate")
    public ResponseEntity<GenerationResponseDto> generate(
            @Valid @RequestBody GenerationRequestDto requestDto) {
        int maxOutputTokens =
                requestDto.getMaxOutputTokens() == null ? 120 : requestDto.getMaxOutputTokens();
        GenerationRequest request = new GenerationRequest(requestDto.getUserId(),
                requestDto.getPrompt(), maxOutputTokens);
        GenerationResponse response = generationOrchestratorService.generate(request);

        GenerationResponseDto responseDto = new GenerationResponseDto();
        responseDto.setUserId(response.getUserId());
        responseDto.setGeneratedText(response.getGeneratedText());
        responseDto.setProcessingTimeMs(response.getProcessingTimeMs());
        responseDto.setCurrentPlan(response.getCurrentPlan());
        responseDto.setConsumedTokens(response.getConsumedTokens());
        responseDto.setPromptTokens(response.getPromptTokens());
        responseDto.setOutputTokensEstimate(response.getOutputTokensEstimate());
        responseDto.setMonthlyTokensUsed(response.getMonthlyTokensUsed());
        responseDto.setMonthlyTokensRemaining(response.getMonthlyTokensRemaining());
        responseDto.setMonthlyResetDate(response.getMonthlyResetDate());
        responseDto.setRequestsUsedInCurrentMinute(response.getRequestsUsedInCurrentMinute());
        responseDto.setRequestsRemainingInCurrentMinute(
                response.getRequestsRemainingInCurrentMinute());

        return ResponseEntity.ok(responseDto);
    }
}
