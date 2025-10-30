package com.example.heartofbucharestsdt;

public class EventBuilder {
    private String name = "Unnamed Event";
    private String location = "Online";
    private boolean isFree = true;

    public EventBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public EventBuilder locatedAt(String location) {
        this.location = location;
        return this;
    }

    public EventBuilder isFree(boolean isFree) {
        this.isFree = isFree;
        return this;
    }

    public Event build() {
        System.out.println("--- Builder: Created new Event ---");
        return new Event(name, location, isFree);
    }
}
