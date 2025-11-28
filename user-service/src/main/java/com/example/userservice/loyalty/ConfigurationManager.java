package com.example.userservice.loyalty;

import java.util.HashMap;
import java.util.Map;

public final class ConfigurationManager {
    private static ConfigurationManager instance;
    private final Map<Integer, String> rewardThresholds;

    private ConfigurationManager() {
        rewardThresholds = new HashMap<>();
        rewardThresholds.put(3, "Free Small Coffee");
        rewardThresholds.put(5, "Free Sandwich");
    }

    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
            System.out.println("[Singleton] ConfigurationManager initialized.");
        }
        return instance;
    }

    public Map<Integer, String> getRewardThresholds() {
        return rewardThresholds;
    }
}
