package com.example.userservice.loyalty;

import com.example.userservice.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoyaltyService {
    private final UserRepository userRepository;
    private RewardAlgorithm strategy = new ParticipationRewardStrategy(); // default

    public LoyaltyService(UserRepository repo) {
        this.userRepository = repo;
    }

    public void setStrategy(RewardAlgorithm strategy) {
        this.strategy = strategy;
    }

    @Transactional
    public void awardPointsForBooking(Long userId) {
        userRepository.findById(userId).ifPresent(u -> {
            int points = strategy.calculatePoints(u);
            u.addPoints(points);
            u.incrementParticipation();
            userRepository.save(u);
            System.out.println("[Loyalty] Awarded " + points + " points to user " + userId);
            // Optionally check thresholds via ConfigurationManager for readable rewards
            ConfigurationManager cm = ConfigurationManager.getInstance();
            String reward = cm.getRewardThresholds().get(u.getParticipationCount());
            if (reward != null) {
                System.out.println("[Loyalty] User " + userId + " reached threshold: " + reward);
            }
        });
    }
}
