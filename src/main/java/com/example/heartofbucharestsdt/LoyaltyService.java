package com.example.heartofbucharestsdt;

public class LoyaltyService {
    private RewardAlgorithm rewardAlgorithm;

    public void setRewardAlgorithm(RewardAlgorithm algorithm) {
        this.rewardAlgorithm = algorithm;
    }

    public Reward determineReward(User user) {
        if (rewardAlgorithm == null) {
            System.out.println("Error: No reward strategy set.");
            return null;
        }
        return rewardAlgorithm.checkReward(user);
    }
}