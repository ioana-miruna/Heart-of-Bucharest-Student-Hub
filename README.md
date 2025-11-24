# ♡ Heart of Bucharest 𐦂𖨆𐀪𖠋𐀪𐀪 

### Team Members  
- Bădică Ioana-Miruna, 1241EA
---

## Project Description  

**Heart of Bucharest** is a **web application** for a students' community-oriented **coffee shop in Bucharest** that is both a **learning and social hub for students**.  
 
As a customer of the coffee shop, you can:
- Discover and apply for **social or educational events**.  
- Earn **rewards** for participation and birthdays.  
- **Book** daily sandwiches, coffees, or snacks.  
- Subscribe to the **newsletter** for updates and offers.  

As an administrator of the coffee shop can:
- **Add and manage events.**  
- **Edit the website content** — banners, menus, and announcements.  
- **Monitor user activity** (accounts, event applications, food bookings).  
- Manage **loyalty bonuses and rewards** for frequent participants.  

---

## System Overview  

The system will be developed as a **web-based platform** using:  

- **Frontend:** HTML, CSS, JavaScript (Thymeleaf)  
- **Backend:** Java + Spring Boot (REST APIs)  
- **Database:** MySQL  

---

## Core Functionalities  

### For Customers  
- **Account Management**  
  - Register and log in securely.  
  - Subscribe to the **newsletter** for updates.  

- **Event Participation**  
  - See upcoming events and apply to attend.  
  - Receive automatic reminders and notifications.  

- **Loyalty & Rewards**  
  - Earn free products for participating to activities and being loyal to the community (e.g., after attending 3, 5, 10 events).  
  - Receive personalized **birthday bonuses**.  

- **Food & Drink Bookings**  
  - Pre-book favorite products (e.g., 2 sandwiches every day).  
  - Manage recurring or one-time bookings easily.  

---

### For Administrators  
- **Event Management**  
  - Create, edit, and delete events.  
  - View who applied for each event.  

- **Content Management**  
  - Edit homepage text, images, menus, and special offers.  

- **Customer Insights**  
  - View total registered users.  
  - Track food bookings and event participation.  
  - Manage rewards and loyalty progress.  

---

## Design Patterns Used  

### 1. **[Singleton](https://refactoring.guru/design-patterns/singleton) – Service Registry & Configuration Manager**  
**Used for:** managing shared services such as database connections, newsletter engine, and reward configuration. Using Singleton I can avoid multiple inconsistent service instances could cause performance issues and errors. 

**Why:**  
- Guarantees a single consistent instance of global services.  
- Prevents redundant connections and synchronization issues.  
- Centralizes configurations like reward thresholds and newsletter frequency.

---

### 2. **[Observer](https://refactoring.guru/design-patterns/observer) – Event Updates & Newsletter Notifications**  
**Used for:** sending updates when events are created or modified; broadcasting newsletters to all subscribers. Using this the system would **not** rely on inefficient polling or manual refresh.  

**Why:**  
- Enables **loose coupling** between event publishers and subscribers.  
- Supports automatic notifications via email or app.  
- Scales easily with new notification channels.
  
---

### 3. **[Builder](https://refactoring.guru/design-patterns/builder) – Event and Booking Creation**  
**Used for:** creating complex event or booking objects with multiple optional parameters. Using Builder I will avoid unclear code and avoid too many constructors.

**Why:**  
- Builds flexible and readable objects step by step.  
- Avoids massive constructors or repetitive setter chains. 

---

### 4. **[Strategy](https://refactoring.guru/design-patterns/strategy) – Ranking and Reward Algorithms**  
**Used for:** Sorting events (by date, popularity, or type) and determining how rewards are granted (e.g., by participation count or purchase frequency). This is the easiest way to add new algorithms without changing all the conditional logic of the base code.  

**Why:**  
- Encapsulates interchangeable ranking and reward algorithms.  
- Allows dynamic behavior changes without modifying base logic.  
- Makes adding new reward strategies simple and maintainable.

# 💡 Architecture Design for "Heart of Bucharest"

This document outlines the architectural decisions for the "Heart of Bucharest" application, comparing a **Monolithic**, a **Microservices**, and an **Event-Driven Architecture (EDA)** approach. The final choice is **Microservices**, as it best balances the academic requirements, complexity, and industry relevance for this project.

---

## 1) Monolithic Architecture

A monolith contains all features in one deployable application (Spring Boot). For Heart of Bucharest that means:

### 💻 Modules within one app :
* **Presentation / API:** (REST controllers used by the frontend/mobile).
* **User module:** (customers, admins, profile, login).
* **Event module:** (create/list events, capacity).
* **Booking module:** (bookings, capacity checks).
* **Notification module:** (email/push).
* **Reward module:** (strategy-based reward computations).
* **Persistence layer:** (single relational DB with tables for users, events, bookings).

### 🔄 Interactions & Data Flow
* **Interactions:** Controllers call service layer → repositories.
* **Communication:** When an event is created, the `EventService` calls `NotificationService` directly (method call). When a booking is created, `BookingService` calls `RewardService` and updates the user in the same DB transaction.
* **Data flow:** Client → API Controller → Service → Repository → DB.
* **Timing:** Notification and rewards happen **synchronously** (or via in-process scheduled tasks).
* This approach is simplest for implementing and demonstrating the required design patterns in a single codebase.

### Diagram: Component and Deployment

![Monolith - Component Diagram](docs/monolith-component.png)
![Monolith - Deployment Diagram](docs/monolith-deployment.png)

### ✔️ Pros
* **Fast to implement:** good for graded projects and POC; fewer moving parts.
* **Simple local dev & debugging:** one process, no network trace complexity.
* **Transactional simplicity:** booking + user updates are **ACID** within single DB.
* **Lower infra/operational burden:** one server, no message broker required.
* **Good for demonstrating design patterns** inside one codebase.

### ❌ Cons
* **Scaling inefficiency:** you must scale whole app even if only notifications need throughput.
* **Deployment risk:** any change requires full redeploy.
* **Tight coupling:** harder to evolve independently (e.g., separate team working on notifications).
* **Long-term maintainability:** as features grow, module boundaries become brittle.

---

## 2) Microservices Architecture - best for this project

Divide functionality into **separately deployable services**, each with its own DB (database-per-service) where appropriate.

### 🛠️ Core Services and Components
* **API Gateway (Edge):** single entry for clients, routing, auth (optionally rate-limiting/caching).
* **User Service:** user registration/login/profile; stores users and reward points.
* **Event Service:** create/list events, event details (**owns events DB**).
* **Booking Service:** book/unbook events, confirm capacity (**owns bookings DB**).
    * When booking, it queries `Event Service` for capacity (synchronous) or uses eventual-consistent reservation pattern (async). After booking it publishes **`BookingCreated`**.
* **Notification Service:** **consumes** `EventCreated` and `BookingCreated` messages to send emails/push.
* **Reward Service:** **consumes** `BookingCreated` (or `AttendanceConfirmed`) and applies a `RewardStrategy` — updates `User Service` via a REST call or publishes `UserPointsUpdated`.
* **Message Broker (RabbitMQ or Kafka):** used for events (Observer). Use asynchronous messages for notifications and reward work; synchronous REST for strongly consistent queries where needed (e.g., capacity check or user validation).
* **Service Discovery / Config / Monitoring:** optional (Eureka/Consul, Config Server, Prometheus/Grafana).
* I will use **RabbitMQ** to keep synchronous REST calls only where immediate consistency is required.

### Diagram: Component and Deployment



[Image of Microservices - Component Diagram]



[Image of Microservices - Deployment Diagram]


### ➡️ Data Flow Examples :
* **Create event:** Client → API Gateway → Event Service (save) → Event Service publishes **`EventCreated`** → Notification Service consumes and emails subscribers.
* **Book event:** Client → API Gateway → Booking Service → (sync) BookingService checks EventService for capacity → BookingService saves booking → BookingService publishes **`BookingCreated`** → Reward Service consumes and updates user points via User Service.

### ✔️ Pros
* **Independent scaling & deployment:** Notification Service can scale separately during campaigns; Booking Service scales on peak sign-ups.
* **Loose coupling & code ownership:** Teams (or you moving to multiple modules) can develop features independently; each service has clear responsibility.
* **Resilience:** failures isolated; e.g., Notification outage does not stop bookings.
* **Extensibility:** new consumers (analytics, search) can subscribe to event streams without changing producers.
* **Technology choice:** you can use specialized tools (e.g., Node for notifications, Java for core services) if needed.
* **Clear demonstration of patterns:** Observer (message broker), Strategy (Reward Service), Builder (entity construction) map well to separate services — great for coursework.

### ❌ Cons
* **Operational complexity:** need CI/CD pipelines for each service, container registry, service discovery, monitoring, logging (ELK/Prometheus). This is overhead for a student project.
* **Distributed complexity:** distributed transactions require sagas or compensation; capacity checks demand careful design (optimistic reservation or pre-reservation with timeouts).
* **Testing complexity:** integration testing across services & broker is heavier — you must set up test harnesses or use contract testing.
* **Cost & time:** more infra and development time than monolith; but aligns with your requirement to implement microservices.

---

## 3) Event-Driven distributed architecture (EDA)

This is a different distributed style from microservices, focusing on **asynchronous events** as the first-class mechanism. It can look similar to microservices but emphasizes event stores and streaming.

### ☁️ Event-centric Organization
* **Producers:** Services like `Event Service` (publishes `EventCreated`), `Booking Service` (publishes `BookingCreated`), `User Service` (publishes `UserUpdated`).
* **Event Broker / Event Store:** **Kafka** (topic-per-event type) is central — durable, ordered event log.
* **Consumers:** `Notification Service`, `Reward Service`, Analytics, Search indexer — they react to streams.
* **Materialized views / read models:** Separate components (or services) build query-optimized databases from event streams (e.g., an EventsReadModel for fast querying).
* **Command side:** Commands (create booking) can be forwarded to command handlers which publish events when completed.

### ➡️ Data flow :
Client issues commands → command handler validates & publishes events → events are appended to the event log → consumers react and update their local state/read models. This style treats events as the source of truth (event sourcing optionally) and provides excellent decoupling.

### Diagram: Component and Deployment




[Image of Event-Driven - Component Diagram]


### ✔️ Pros
* **Extreme decoupling & extensibility:** new analytic or notification consumers can be added without touching producers — very good for future features (search, recommendations).
* **High throughput & replayability:** Kafka provides durable logs and ability to rebuild read models — useful for analytics about volunteering patterns.
* **Natural fit for asynchronous tasks:** email campaigns, recommendation pipelines, and offline analytics are elegantly supported.
* **Audit & history:** event logs provide complete history (valuable for later research/analysis).

### ❌ Cons
* **Steep learning curve & infra needs:** Kafka clusters, schema registry (Avro/Protobuf), retention policies — more overhead than simple microservices with RabbitMQ.
* **Eventual consistency & UX complexity:** you must design UI feedback for asynchronous state (e.g., booking pending until confirmed).
* **Complex testing and debugging:** tracing an event across topics requires good observability tooling.
* **Possibly overkill** for a course project unless you need heavy analytics or replay features.

---

## 📊 Comparison

| Aspect | **Monolith** | **Microservices** | **Event-Driven** |
| :--- | :--- | :--- | :--- |
| **Implementation speed (POC)** | High | Medium | Low |
| **Operational complexity** | Low | Medium–High | High |
| **Scalability** | Low | High | Very high |
| **Extensibility** | Medium | High | Very high |
| **Transactional simplicity** | High | Medium (sagas) | Low (sagas/events) |
| **Observability difficulty** | Low | Medium | High |
| **Suitability for course graded project** | High | High (if required) | Medium–Low (unless analytics focus) |

---

## 🏆 Which is best for my project and why

For my project, **Microservices architecture** is the most suitable choice because:

* **Matches assignment requirement:** I must implement microservices — so pick an approach that is practical to implement within time and demonstrates architectural concepts.
* **Balanced complexity:** Microservices give separation and scalability without the full complexity of an event-sourcing Kafka ecosystem. **RabbitMQ + Spring Boot microservices** gives a manageable learning curve.
* **Good mapping to domain:** Each domain area (Users, Events, Bookings, Notifications, Rewards) becomes a natural independent service. This maps to the course requirement to showcase design patterns across services (Observer via broker, Strategy in Reward Service, Builder in Event/Booking models).
* **Incremental delivery:** you can start with a small set of services (User, Event, Booking, Notification, Reward) and expand; you can run locally with docker-compose + RabbitMQ (I provided a starter earlier).
* **Academic depth & industry relevance:** Microservices let you demonstrate distributed design, inter-service communication patterns, and trade-offs (sagas, eventual consistency) — strong material for the report & evaluation.
