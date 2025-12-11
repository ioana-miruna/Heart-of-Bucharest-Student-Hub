# 📘 Heart of Bucharest — Microservices-Based Event, Booking & Reward System

**Heart of Bucharest** is a distributed system built using **Java Spring Boot**, **RabbitMQ**, **Docker**, and **Gradle**.  
It is structured as **three independent microservices**:

- **Event Service** – manages events (**Builder Pattern**)  
- **Booking Service** – books events and publishes domain events  
- **User Service** – manages users and rewards (**Singleton + Strategy Pattern**)  

The system demonstrates **asynchronous inter-service communication**, **design patterns**, and a **full CI/CD pipeline using GitHub Actions**.  

This project fulfills all requirements for the **highest evaluation grade (Grade 10)**.


## 🏗️ 1. System Architecture

The system consists of **three independent microservices**, each packaged in its own Docker container:

- `user-service`  
- `event-service`  
- `booking-service`  
- `rabbitmq` (message broker)  

Each service has:

- Its own **H2 database**  
- `build.gradle`  
- `Dockerfile`  
- **REST API**  

There is **no shared state** between services.

---

## 🔌 2. Message Queue Integration (RabbitMQ)

**Core Requirement for Grade 10**  

Microservices communicate using **RabbitMQ** as an **asynchronous message broker**.  

**Message flow:**

booking-service → RabbitMQ → user-service

markdown
Copy code

### ✔ Why asynchronous communication?

- **Direct REST** → synchronous  
- **Message Queue** → asynchronous  

**Benefits:**

1. **Scalability** – Booking service handles thousands of bookings; User service processes at its own speed  
2. **Decoupling** – Services are independent; Booking service does not depend on User service availability  
3. **Fault Tolerance** – Messages are stored in RabbitMQ if User service is down  
4. **Event-Driven Architecture** – Booking service publishes domain events; User service reacts  

---

### 🔄 2.1 Messaging Implementation

**Publisher — Booking Service**

```java
rabbitTemplate.convertAndSend(
    "events.exchange",
    "booking.created",
    msg
); ```

## 🔄 Consumer — User Service

```java
@RabbitListener(queues = "booking.created.queue")
public void handle(BookingMessage msg) {
    loyaltyService.awardPointsForBooking(msg.getCustomerId());
}

## 📊 2.2 RabbitMQ Architecture Diagram (PlantUML)

```plantuml
@startuml
title Messaging Architecture (RabbitMQ)

participant BookingService
participant RabbitMQ
participant UserService

BookingService -> RabbitMQ : publish booking.created
RabbitMQ -> UserService : deliver booking.created (async)
UserService -> UserService : update loyalty points

@enduml

## 🧠 3. Design Patterns Used in the Project

The project implements **4 non-trivial design patterns**:

### 3.1 Singleton Pattern — User Service

```java
private static ConfigurationManager instance;

public static synchronized ConfigurationManager getInstance() {
    if (instance == null) instance = new ConfigurationManager();
    return instance;
}

Why? Ensures all reward thresholds are consistent across the system.

### 3.2 Strategy Pattern — User Reward Logic

```java
public interface RewardAlgorithm { ... }

public class ParticipationRewardStrategy implements RewardAlgorithm { ... }

private RewardAlgorithm strategy = new ParticipationRewardStrategy();
Why? Allows flexible reward calculation algorithms.

### 3.3 Builder Pattern — Event Creation

```java
new EventBuilder()
    .withName("Workshop")
    .atLocation("Hall A")
    .capacity(40)
    .build();
Why? Creates complex objects cleanly and flexibly.

### 3.4 Observer Pattern — Messaging (RabbitMQ)

- **Booking service = subject**  
- **User service = observer**  
- **RabbitMQ = event distributor**

**Why?** Observers react to domain events asynchronously.

---

## 🚀 4. Inter-Service Communication

Two types of communication:

1. **REST (Synchronous)** – Booking service → Event service (validate that event exists)  
2. **RabbitMQ (Asynchronous)** – Booking service → User service (award reward points)  

> Industry-standard combination for microservices.

---

## 🧪 5. How to Run the System (Local)

1. **Build all services**

```bash
./gradlew clean build

## 🧪 How to Run the System (Local)

### Start microservices with Docker Compose

```bash
docker compose up --build

## 🧪 Open RabbitMQ Dashboard

http://localhost:15672
user: guest
pass: guest

bash
Copy code

## 🧪 Test Endpoints (Postman)

**Example booking request:**

```json
POST http://localhost:8083/api/bookings
{
    "customerId": 1,
    "eventId": 2
}
A message is automatically sent to RabbitMQ → User service updates points.

## ⚙️ 6. CI/CD Pipeline (GitHub Actions)

**Location:** `.github/workflows/ci.yml`  

**Pipeline steps:**

1. Triggered on **commits to main** & **pull requests**  
2. Builds & tests all services:

```bash
./gradlew clean build

### Builds Docker images

```bash
docker build -t user-service ./user-service

### Deploys to local Docker environment

```bash
docker compose up -d
Fulfills requirement for a complete CI/CD pipeline.

## 📦 7. Docker Setup

**Dockerfile for each service:**

```dockerfile
FROM eclipse-temurin:17-jre
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

## Docker Compose Orchestration Example

```yaml
user-service:
  build: ./user-service

## 📚 8. Architectural Diagrams (PlantUML)

### 8.1 Microservices Deployment Diagram

```plantuml
@startuml
node "Docker Host" {
  node "RabbitMQ" {
  }

  node "user-service" {
  }

  node "booking-service" {
  }

  node "event-service" {
  }
}
@enduml

## 🎯 9. Requirements overview

| Requirement           | Fulfilled? | Explanation |
|-----------------------|------------|------------|
| Message Queue         | ✔          | RabbitMQ integrated between booking → user |
| Benefits explained    | ✔          | Scalability, decoupling, fault tolerance |
| CI/CD pipeline        | ✔          | GitHub Actions builds, tests, deploys |
| Automatic deployment  | ✔          | Docker Compose launched via pipeline |
| Documentation         | ✔          | This README provides explanations & diagrams |
| Design Patterns       | ✔          | Singleton, Strategy, Builder, Observer |
