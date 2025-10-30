package com.example.heartofbucharestsdt;

public class Event {
    private String name;
    private String location;
    private boolean isFree;

    Event(String name, String location, boolean isFree) {
        this.name = name;
        this.location = location;
        this.isFree = isFree;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Event: " + name + " at " + location + " (Free: " + isFree + ")";
    }
}