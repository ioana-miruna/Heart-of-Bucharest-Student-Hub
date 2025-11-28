package com.example.bookingservice.web;

import com.example.bookingservice.messaging.BookingMessage;
import com.example.bookingservice.messaging.BookingPublisher;
import com.example.bookingservice.model.Booking;
import com.example.bookingservice.repo.BookingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingRepository repo;
    private final BookingPublisher publisher;
    private final RestTemplate rest = new RestTemplate();

    public BookingController(BookingRepository repo, BookingPublisher publisher){
        this.repo = repo; this.publisher = publisher;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateBookingRequest req) {
        String eventServiceUrl = "http://event-service:8082/api/events/" + req.eventId;
        try {
            ResponseEntity<EventResponse> resp = rest.getForEntity(eventServiceUrl, EventResponse.class);
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                return ResponseEntity.badRequest().body("Event not found");
            }
            EventResponse ev = resp.getBody();
            if (ev.getCapacity() <= 0) {
                return ResponseEntity.status(409).body("Event full");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error contacting event service: " + ex.getMessage());
        }

        Booking b = new Booking(req.customerId, req.eventId);
        Booking saved = repo.save(b);

        BookingMessage msg = new BookingMessage(saved.getId(), saved.getCustomerId(), saved.getEventId());
        publisher.publishBookingCreated(msg);

        return ResponseEntity.created(URI.create("/api/bookings/" + saved.getId())).body(saved);
    }

    public static class CreateBookingRequest {
        public Long customerId;
        public Long eventId;
    }

    public static class EventResponse {
        private Long id;
        private String name;
        private String location;
        private boolean isFree;
        private int capacity;
        public EventResponse() {}
        public Long getId(){ return id; }
        public String getName(){ return name; }
        public String getLocation(){ return location; }
        public boolean getIsFree(){ return isFree; }
        public int getCapacity(){ return capacity; }
        public void setId(Long id){ this.id = id; }
        public void setName(String n){ this.name = n; }
        public void setLocation(String l){ this.location = l; }
        public void setIsFree(boolean f){ this.isFree = f; }
        public void setCapacity(int c){ this.capacity = c; }
    }
}
