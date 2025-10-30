package com.example.heartofbucharestsdt;

import java.util.ArrayList;
import java.util.List;

public class EventPublisher {
    private final List<EventListener> listeners = new ArrayList<>();

    public void addListener(EventListener listener) {
        listeners.add(listener);
        System.out.println("[Publisher] New listener registered.");
    }

    public void publishEvent(Event event) {
        System.out.println("\n--- Observer: Notifying Listeners about " + event.getName() + " ---");
        for (EventListener listener : listeners) {
            listener.notify(event);
        }
    }
}