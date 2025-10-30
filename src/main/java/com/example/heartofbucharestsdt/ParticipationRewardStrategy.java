package com.example.heartofbucharestsdt;

import java.util.Map;

public class ParticipationRewardStrategy implements RewardAlgorithm {
    @Override
    public Reward checkReward(User user) {
        int count = user.getParticipationCount();

        Map<Integer, Reward> thresholds = ConfigurationManager.getInstance().getRewardThresholds();

        if (thresholds.containsKey(count)) {
            System.out.println("[Strategy] Found reward for " + count + " participations.");
            return thresholds.get(count);
        }
        return null;
    }
}