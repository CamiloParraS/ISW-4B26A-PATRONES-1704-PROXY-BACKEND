package com.slop.gpt.model;

public class TokenUsageEstimate {
    private final int promptTokens;
    private final int outputTokens;
    private final int totalTokens;

    public TokenUsageEstimate(int promptTokens, int outputTokens, int totalTokens) {
        this.promptTokens = promptTokens;
        this.outputTokens = outputTokens;
        this.totalTokens = totalTokens;
    }

    public int getPromptTokens() {
        return promptTokens;
    }

    public int getOutputTokens() {
        return outputTokens;
    }

    public int getTotalTokens() {
        return totalTokens;
    }
}
