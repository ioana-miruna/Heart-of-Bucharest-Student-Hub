package com.example.userservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private int points = 0;
    private int participationCount = 0;

    public User() {}

    public User(String name, String email) {
        this.name = name; this.email = email;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getPoints() { return points; }
    public void addPoints(int p) { this.points += p; }
    public int getParticipationCount(){ return participationCount; }
    public void incrementParticipation(){ this.participationCount++; }
}
