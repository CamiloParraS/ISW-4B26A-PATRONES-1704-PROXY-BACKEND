package com.slop.gpt.proxy;

import com.slop.gpt.model.GenerationRequest;
import com.slop.gpt.model.GenerationResponse;
import com.slop.gpt.model.RateLimitDecision;
import com.slop.gpt.service.RateLimitService;

public class RateLimitProxyService implements AIGenerationService {
    private final AIGenerationService delegate;
    private final RateLimitService rateLimitService;

    public RateLimitProxyService(AIGenerationService delegate, RateLimitService rateLimitService) {
        this.delegate = delegate;
        this.rateLimitService = rateLimitService;
    }

    @Override
    public GenerationResponse generate(GenerationRequest request) {
        RateLimitDecision decision = rateLimitService.consumeRequestSlot(request.getUserId());
        request.setRequestsUsedInCurrentMinute(decision.getRequestsUsedInCurrentMinute());
        request.setRequestsRemainingInCurrentMinute(decision.getRequestsRemainingInCurrentMinute());

        GenerationResponse response = delegate.generate(request);
        response.setRequestsUsedInCurrentMinute(decision.getRequestsUsedInCurrentMinute());
        response.setRequestsRemainingInCurrentMinute(
                decision.getRequestsRemainingInCurrentMinute());
        return response;
    }
}
