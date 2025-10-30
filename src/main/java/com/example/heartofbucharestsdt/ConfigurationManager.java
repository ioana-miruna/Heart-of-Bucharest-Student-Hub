package com.example.heartofbucharestsdt;

import java.util.HashMap;
import java.util.Map;

public final class ConfigurationManager {
    private static ConfigurationManager instance;
    private final Map<Integer, Reward> rewardThresholds;

    private ConfigurationManager() {
        rewardThresholds = new HashMap<>();
        rewardThresholds.put(3, new Reward("Free Small Coffee"));
        rewardThresholds.put(5, new Reward("Free Sandwich"));
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
            System.out.println("--- Singleton: Configuration Manager Initialized ---");
        }
        return instance;
    }

    public Map<Integer, Reward> getRewardThresholds() {
        return rewardThresholds;
    }
}