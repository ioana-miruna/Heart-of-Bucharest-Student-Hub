package com.example.eventservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private boolean isFree;
    private int capacity;
    private LocalDateTime dateTime;

    public Event() {}

    public Event(String name, String location, boolean isFree, int capacity) {
        this.name = name; this.location = location; this.isFree = isFree; this.capacity = capacity;
        this.dateTime = LocalDateTime.now();
    }

    public Long getId(){ return id; }
    public String getName(){ return name; }
    public String getLocation(){ return location; }
    public boolean isFree(){ return isFree; }
    public int getCapacity(){ return capacity; }

    public void setCapacity(int capacity) { this.capacity = capacity; }
}
