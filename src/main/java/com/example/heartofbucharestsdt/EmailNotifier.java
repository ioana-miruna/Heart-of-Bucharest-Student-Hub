package com.example.heartofbucharestsdt;

public class EmailNotifier implements EventListener {
    @Override
    public void notify(Event event) {
        System.out.println("[Email] Sending email: New event '" + event.getName() + "' is ready!");
    }
}