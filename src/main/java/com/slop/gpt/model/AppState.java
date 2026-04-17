package com.slop.gpt.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppState {
    private Map<String, UserAccount> users;
    private Map<String, RateLimitWindow> rateLimitCounters;
    private List<UpgradeHistoryEntry> upgradeHistory;

    public AppState() {
        this.users = new HashMap<>();
        this.rateLimitCounters = new HashMap<>();
        this.upgradeHistory = new ArrayList<>();
    }

    public Map<String, UserAccount> getUsers() {
        return users;
    }

    public void setUsers(Map<String, UserAccount> users) {
        this.users = users;
    }

    public Map<String, RateLimitWindow> getRateLimitCounters() {
        return rateLimitCounters;
    }

    public void setRateLimitCounters(Map<String, RateLimitWindow> rateLimitCounters) {
        this.rateLimitCounters = rateLimitCounters;
    }

    public List<UpgradeHistoryEntry> getUpgradeHistory() {
        return upgradeHistory;
    }

    public void setUpgradeHistory(List<UpgradeHistoryEntry> upgradeHistory) {
        this.upgradeHistory = upgradeHistory;
    }
}
