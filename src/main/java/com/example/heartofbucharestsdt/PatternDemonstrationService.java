package com.example.heartofbucharestsdt;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class PatternDemonstrationService {

    @PostConstruct
    public void runPatternDemo() {
        System.out.println("  HEART OF BUCHAREST - DESIGN PATTERN DEMO START");

        System.out.println("\n--- 1. SYSTEM SETUP: Singleton & Observer Initialization ---");
        ConfigurationManager config = ConfigurationManager.getInstance();
        System.out.println("Config Hash: " + config.hashCode() + " (Always the same instance)");

        EventPublisher publisher = new EventPublisher();
        publisher.addListener(new EmailNotifier());

        User student = new User("Jojo");

        LoyaltyService loyaltyService = new LoyaltyService();
        loyaltyService.setRewardAlgorithm(new ParticipationRewardStrategy());

        System.out.println("\n--- 2. USE CASE: Admin Creates Event (Builder) ---");

        Event studySession = new EventBuilder()
                .withName("Advanced Java Topics")
                .locatedAt("Main Lecture Hall")
                .isFree(true)
                .build();

        System.out.println("Event Created: " + studySession);

        publisher.publishEvent(studySession);

        System.out.println("\n--- 3. USE CASE: Customer Earns Reward (Strategy & Singleton) ---");

        student.incrementParticipation();
        student.incrementParticipation();
        student.incrementParticipation();

        System.out.println(student.getName() + " has " + student.getParticipationCount() + " participations.");

        Reward reward1 = loyaltyService.determineReward(student);
        if (reward1 != null) {
            System.out.println("🎉 **SUCCESS!** Reward earned: " + reward1);
        } else {
            System.out.println("No reward earned at this count.");
        }
    }
}