package com.slop.gpt.model;

public class UpgradeHistoryEntry {
    private String userId;
    private Plan fromPlan;
    private Plan toPlan;
    private String upgradedAt;

    public UpgradeHistoryEntry() {}

    public UpgradeHistoryEntry(String userId, Plan fromPlan, Plan toPlan, String upgradedAt) {
        this.userId = userId;
        this.fromPlan = fromPlan;
        this.toPlan = toPlan;
        this.upgradedAt = upgradedAt;
    }

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
