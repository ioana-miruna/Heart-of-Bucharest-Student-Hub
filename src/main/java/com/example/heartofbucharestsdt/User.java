package com.example.heartofbucharestsdt;

public class User {
    private String name;
    private int participationCount;

    public User(String name) {
        this.name = name;
        this.participationCount = 0;
    }

    public String getName() {
        return name;
    }

    public int getParticipationCount() {
        return participationCount;
    }

    public void incrementParticipation() {
        this.participationCount++;
    }
}
