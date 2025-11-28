package com.example.bookingservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private Long eventId;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Booking() {}
    public Booking(Long customerId, Long eventId) {
        this.customerId = customerId; this.eventId = eventId;
    }

    public Long getId(){ return id; }
    public Long getCustomerId(){ return customerId; }
    public Long getEventId(){ return eventId; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
}
