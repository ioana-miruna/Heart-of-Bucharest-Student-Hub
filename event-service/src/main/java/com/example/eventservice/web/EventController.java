package com.example.eventservice.web;

import com.example.eventservice.model.Event;
import com.example.eventservice.repo.EventRepository;
import com.example.eventservice.messaging.EventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventRepository repo;
    private final EventPublisher publisher;
    public EventController(EventRepository repo, EventPublisher publisher){
        this.repo = repo;
        this.publisher = publisher;
    }

    @PostMapping
    public ResponseEntity<Event> create(@RequestBody CreateEventRequest req) {
        Event e = new Event(req.name, req.location, req.isFree, req.capacity);
        Event saved = repo.save(e);
        publisher.publishEventCreated(saved); // publish EventCreated if needed
        return ResponseEntity.created(URI.create("/api/events/" + saved.getId())).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> get(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    public static class CreateEventRequest {
        public String name;
        public String location;
        public boolean isFree = true;
        public int capacity = 10;
    }
}
