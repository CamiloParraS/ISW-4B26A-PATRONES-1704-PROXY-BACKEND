package com.slop.gpt.dto;

import com.slop.gpt.model.Plan;

public class UpgradePlanResponseDto {
    private String userId;
    private Plan fromPlan;
    private Plan toPlan;
    private String upgradedAt;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Plan getFromPlan() {
        return fromPlan;
    }

    public void setFromPlan(Plan fromPlan) {
        this.fromPlan = fromPlan;
    }

    public Plan getToPlan() {
        return toPlan;
    }

    public void setToPlan(Plan toPlan) {
        this.toPlan = toPlan;
    }

    public String getUpgradedAt() {
        return upgradedAt;
    }

    public void setUpgradedAt(String upgradedAt) {
        this.upgradedAt = upgradedAt;
    }
}
