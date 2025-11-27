package com.example.userservice.web;

import com.example.userservice.model.User;
import com.example.userservice.repo.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository repo;
    public UserController(UserRepository repo) { this.repo = repo; }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User u) {
        User saved = repo.save(u);
        return ResponseEntity.created(URI.create("/api/users/" + saved.getId())).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<Integer> getPoints(@PathVariable Long id) {
        return repo.findById(id).map(u -> ResponseEntity.ok(u.getPoints()))
                .orElse(ResponseEntity.notFound().build());
    }
}
