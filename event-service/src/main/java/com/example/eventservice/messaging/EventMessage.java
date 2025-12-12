package com.example.eventservice.messaging;

public class EventMessage {
    private Long id;
    private String name;
    private int capacity;
    public EventMessage() {}
    public EventMessage(Long id, String name, int capacity) {
        this.id = id; this.name = name; this.capacity = capacity;
    }
    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }
    public String getName(){ return name; }
    public int getCapacity(){ return capacity; }
}
