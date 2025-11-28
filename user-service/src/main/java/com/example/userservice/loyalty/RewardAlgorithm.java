package com.example.userservice.loyalty;

import com.example.userservice.model.User;

public interface RewardAlgorithm {
    int calculatePoints(User user);
}
