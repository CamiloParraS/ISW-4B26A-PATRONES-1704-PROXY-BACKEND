package com.slop.gpt.proxy;

import com.slop.gpt.model.GenerationRequest;
import com.slop.gpt.model.GenerationResponse;

public interface AIGenerationService {
    GenerationResponse generate(GenerationRequest request);
}
