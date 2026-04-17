package com.slop.gpt.config;

import com.slop.gpt.proxy.AIGenerationService;
import com.slop.gpt.proxy.MockAIGenerationService;
import com.slop.gpt.proxy.QuotaProxyService;
import com.slop.gpt.proxy.RateLimitProxyService;
import com.slop.gpt.service.QuotaService;
import com.slop.gpt.service.RateLimitService;
import com.slop.gpt.service.TokenCalculatorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;

@Configuration
public class ProxyChainConfiguration {
    @Bean
    public Clock applicationClock() {
        return Clock.systemDefaultZone();
    }

    @Bean("mockAIGenerationService")
    public AIGenerationService mockAIGenerationService() {
        return new MockAIGenerationService();
    }

    @Bean("quotaProxyService")
    public AIGenerationService quotaProxyService(
            @Qualifier("mockAIGenerationService") AIGenerationService delegate,
            QuotaService quotaService, TokenCalculatorService tokenCalculatorService) {
        return new QuotaProxyService(delegate, quotaService, tokenCalculatorService);
    }

    @Bean("rateLimitProxyService")
    public AIGenerationService rateLimitProxyService(
            @Qualifier("quotaProxyService") AIGenerationService delegate,
            RateLimitService rateLimitService) {
        return new RateLimitProxyService(delegate, rateLimitService);
    }

    @Primary
    @Bean
    public AIGenerationService aiGenerationService(
            @Qualifier("rateLimitProxyService") AIGenerationService chain) {
        return chain;
    }
}
