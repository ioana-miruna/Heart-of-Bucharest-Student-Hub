package com.example.heartofbucharestsdt;

public class Reward {
    private String description;

    public Reward(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "✨ " + description + " ✨";
    }
}