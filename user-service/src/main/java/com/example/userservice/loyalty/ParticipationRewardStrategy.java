package com.example.userservice.loyalty;

import com.example.userservice.model.User;

public class ParticipationRewardStrategy implements RewardAlgorithm {
    @Override
    public int calculatePoints(User user) {
        // simple example: 5 points per booking; could be based on participation count or thresholds
        return 5;
    }
}
