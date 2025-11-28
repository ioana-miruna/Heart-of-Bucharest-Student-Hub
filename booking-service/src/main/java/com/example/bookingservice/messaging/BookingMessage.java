package com.example.bookingservice.messaging;

public class BookingMessage {
    private Long id;
    private Long customerId;
    private Long eventId;
    public BookingMessage() {}
    public BookingMessage(Long id, Long customerId, Long eventId) {
        this.id = id; this.customerId = customerId; this.eventId = eventId;
    }
    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }
    public Long getCustomerId(){ return customerId; }
    public void setCustomerId(Long customerId){ this.customerId = customerId; }
    public Long getEventId(){ return eventId; }
    public void setEventId(Long eventId){ this.eventId = eventId; }
}
