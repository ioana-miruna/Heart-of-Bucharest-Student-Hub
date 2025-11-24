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

# [cite_start]💡 Architecture Design for "Heart of Bucharest" [cite: 1]

[cite_start]This document outlines the architectural decisions for the "Heart of Bucharest" application, comparing a **Monolithic**, a **Microservices**, and an **Event-Driven Architecture (EDA)** approach[cite: 1]. [cite_start]The final choice is **Microservices**, as it best balances the academic requirements, complexity, and industry relevance for this project[cite: 83].

---

## [cite_start]1) Monolithic Architecture [cite: 2]

[cite_start]A monolith contains all features in one deployable application (Spring Boot)[cite: 3]. [cite_start]For Heart of Bucharest that means: [cite: 3]

### [cite_start]💻 Modules within one app : [cite: 4]
* [cite_start]**Presentation / API:** (REST controllers used by the frontend/mobile)[cite: 5].
* [cite_start]**User module:** (customers, admins, profile, login)[cite: 6].
* [cite_start]**Event module:** (create/list events, capacity)[cite: 7].
* [cite_start]**Booking module:** (bookings, capacity checks)[cite: 8].
* [cite_start]**Notification module:** (email/push)[cite: 9].
* [cite_start]**Reward module:** (strategy-based reward computations)[cite: 10].
* [cite_start]**Persistence layer:** (single relational DB with tables for users, events, bookings)[cite: 11].

### 🔄 Interactions & Data Flow
* [cite_start]**Interactions:** Controllers call service layer → repositories[cite: 12].
* [cite_start]**Communication:** When an event is created, the `EventService` calls `NotificationService` directly (method call)[cite: 12]. [cite_start]When a booking is created, `BookingService` calls `RewardService` and updates the user in the same DB transaction[cite: 13].
* [cite_start]**Data flow:** Client → API Controller → Service → Repository → DB[cite: 14].
* [cite_start]**Timing:** Notification and rewards happen **synchronously** (or via in-process scheduled tasks)[cite: 15].
* [cite_start]This approach is simplest for implementing and demonstrating the required design patterns in a single codebase[cite: 16].

### Diagram: Component and Deployment



[Image of Monolith - Component Diagram]



### [cite_start]✔️ Pros [cite: 17]
* [cite_start]**Fast to implement:** good for graded projects and POC; fewer moving parts[cite: 18].
* [cite_start]**Simple local dev & debugging:** one process, no network trace complexity[cite: 19].
* [cite_start]**Transactional simplicity:** booking + user updates are **ACID** within single DB[cite: 20].
* [cite_start]**Lower infra/operational burden:** one server, no message broker required[cite: 21].
* [cite_start]**Good for demonstrating design patterns** inside one codebase[cite: 22].

### [cite_start]❌ Cons [cite: 23]
* [cite_start]**Scaling inefficiency:** you must scale whole app even if only notifications need throughput[cite: 24].
* [cite_start]**Deployment risk:** any change requires full redeploy[cite: 25].
* [cite_start]**Tight coupling:** harder to evolve independently (e.g., separate team working on notifications)[cite: 26].
* [cite_start]**Long-term maintainability:** as features grow, module boundaries become brittle[cite: 27].

---

## [cite_start]2) Microservices Architecture - best for this project [cite: 28]

[cite_start]Divide functionality into **separately deployable services**, each with its own DB (database-per-service) where appropriate[cite: 29].

### [cite_start]🛠️ Core Services and Components [cite: 30]
* [cite_start]**API Gateway (Edge):** single entry for clients, routing, auth (optionally rate-limiting/caching)[cite: 31].
* [cite_start]**User Service:** user registration/login/profile; stores users and reward points[cite: 32].
* [cite_start]**Event Service:** create/list events, event details (**owns events DB**)[cite: 33].
* [cite_start]**Booking Service:** book/unbook events, confirm capacity (**owns bookings DB**)[cite: 34].
    * [cite_start]When booking, it queries `Event Service` for capacity (synchronous) or uses eventual-consistent reservation pattern (async)[cite: 35]. [cite_start]After booking it publishes **`BookingCreated`**[cite: 35].
* [cite_start]**Notification Service:** **consumes** `EventCreated` and `BookingCreated` messages to send emails/push[cite: 36].
* [cite_start]**Reward Service:** **consumes** `BookingCreated` (or `AttendanceConfirmed`) and applies a `RewardStrategy` — updates `User Service` via a REST call or publishes `UserPointsUpdated`[cite: 37].
* [cite_start]**Message Broker (RabbitMQ or Kafka):** used for events (Observer)[cite: 38]. [cite_start]Use asynchronous messages for notifications and reward work [cite: 38][cite_start]; synchronous REST for strongly consistent queries where needed (e.g., capacity check or user validation)[cite: 39].
* [cite_start]**Service Discovery / Config / Monitoring:** optional (Eureka/Consul, Config Server, Prometheus/Grafana)[cite: 40].
* [cite_start]I will use **RabbitMQ** to keep synchronous REST calls only where immediate consistency is required[cite: 44].

### Diagram: Component and Deployment



[Image of Microservices - Component Diagram]



[Image of Microservices - Deployment Diagram]


### [cite_start]➡️ Data Flow Examples : [cite: 41]
* [cite_start]**Create event:** Client → API Gateway → Event Service (save) → Event Service publishes **`EventCreated`** → Notification Service consumes and emails subscribers[cite: 42].
* [cite_start]**Book event:** Client → API Gateway → Booking Service → (sync) BookingService checks EventService for capacity → BookingService saves booking → BookingService publishes **`BookingCreated`** → Reward Service consumes and updates user points via User Service[cite: 43].

### [cite_start]✔️ Pros [cite: 45]
* **Independent scaling & deployment:** Notification Service can scale separately during campaigns; [cite_start]Booking Service scales on peak sign-ups[cite: 46].
* [cite_start]**Loose coupling & code ownership:** Teams (or you moving to multiple modules) can develop features independently [cite: 47][cite_start]; each service has clear responsibility[cite: 48].
* [cite_start]**Resilience:** failures isolated; e.g., Notification outage does not stop bookings[cite: 49].
* [cite_start]**Extensibility:** new consumers (analytics, search) can subscribe to event streams without changing producers[cite: 50].
* [cite_start]**Technology choice:** you can use specialized tools (e.g., Node for notifications, Java for core services) if needed[cite: 51].
* [cite_start]**Clear demonstration of patterns:** Observer (message broker), Strategy (Reward Service), Builder (entity construction) map well to separate services — great for coursework[cite: 52].

### [cite_start]❌ Cons [cite: 53]
* [cite_start]**Operational complexity:** need CI/CD pipelines for each service, container registry, service discovery, monitoring, logging (ELK/Prometheus)[cite: 54]. [cite_start]This is overhead for a student project[cite: 55].
* [cite_start]**Distributed complexity:** distributed transactions require sagas or compensation; capacity checks demand careful design (optimistic reservation or pre-reservation with timeouts)[cite: 56].
* [cite_start]**Testing complexity:** integration testing across services & broker is heavier — you must set up test harnesses or use contract testing[cite: 57].
* [cite_start]**Cost & time:** more infra and development time than monolith; but aligns with your requirement to implement microservices[cite: 58].

---

## [cite_start]3) Event-Driven distributed architecture (EDA) [cite: 59]

[cite_start]This is a different distributed style from microservices, focusing on **asynchronous events** as the first-class mechanism[cite: 60]. [cite_start]It can look similar to microservices but emphasizes event stores and streaming[cite: 61].

### [cite_start]☁️ Event-centric Organization [cite: 62]
* [cite_start]**Producers:** `Event Service` (publishes `EventCreated`), `Booking Service` (publishes `BookingCreated`), `User Service` (publishes `UserUpdated`)[cite: 63].
* [cite_start]**Event Broker / Event Store:** **Kafka** (topic-per-event type) is central — durable, ordered event log[cite: 64].
* [cite_start]**Consumers:** `Notification Service`, `Reward Service`, Analytics, Search indexer — they react to streams[cite: 65].
* [cite_start]**Materialized views / read models:** Separate components (or services) build query-optimized databases from event streams (e.g., an EventsReadModel for fast querying)[cite: 66].
* [cite_start]**Command side:** Commands (create booking) can be forwarded to command handlers which publish events when completed[cite: 67].

### [cite_start]➡️ Data flow : [cite: 68]
[cite_start]Client issues commands → command handler validates & publishes events → events are appended to the event log → consumers react and update their local state/read models[cite: 68]. [cite_start]This style treats events as the source of truth (event sourcing optionally) and provides excellent decoupling[cite: 69].

### Diagram: Component and Deployment




[Image of Event-Driven - Component Diagram]


### [cite_start]✔️ Pros [cite: 70]
* [cite_start]**Extreme decoupling & extensibility:** new analytic or notification consumers can be added without touching producers — very good for future features (search, recommendations)[cite: 71].
* [cite_start]**High throughput & replayability:** Kafka provides durable logs and ability to rebuild read models — useful for analytics about volunteering patterns[cite: 72].
* [cite_start]**Natural fit for asynchronous tasks:** email campaigns, recommendation pipelines, and offline analytics are elegantly supported[cite: 73].
* [cite_start]**Audit & history:** event logs provide complete history (valuable for later research/analysis)[cite: 74].

### [cite_start]❌ Cons [cite: 75]
* [cite_start]**Steep learning curve & infra needs:** Kafka clusters, schema registry (Avro/Protobuf), retention policies — more overhead than simple microservices with RabbitMQ[cite: 76].
* [cite_start]**Eventual consistency & UX complexity:** you must design UI feedback for asynchronous state (e.g., booking pending until confirmed)[cite: 77].
* [cite_start]**Complex testing and debugging:** tracing an event across topics requires good observability tooling[cite: 78].
* [cite_start]**Possibly overkill** for a course project unless you need heavy analytics or replay features[cite: 79].

---

## [cite_start]📊 Comparison [cite: 80]

| Aspect | **Monolith** | **Microservices** | **Event-Driven** |
| :--- | :--- | :--- | :--- |
| **Implementation speed (POC)** | [cite_start]High [cite: 81] | [cite_start]Medium [cite: 81] | [cite_start]Low [cite: 81] |
| **Operational complexity** | [cite_start]Low [cite: 81] | [cite_start]Medium–High [cite: 81] | [cite_start]High [cite: 81] |
| **Scalability** | [cite_start]Low [cite: 81] | [cite_start]High [cite: 81] | [cite_start]Very high [cite: 81] |
| **Extensibility** | [cite_start]Medium [cite: 81] | [cite_start]High [cite: 81] | [cite_start]Very high [cite: 81] |
| **Transactional simplicity** | [cite_start]High [cite: 81] | [cite_start]Medium (sagas) [cite: 81] | [cite_start]Low (sagas/events) [cite: 81] |
| **Observability difficulty** | [cite_start]Low [cite: 81] | [cite_start]Medium [cite: 81] | [cite_start]High [cite: 81] |
| **Suitability for course graded project** | [cite_start]High [cite: 81] | [cite_start]High (if required) [cite: 81] | [cite_start]Medium–Low (unless analytics focus) [cite: 81] |

---

## [cite_start]🏆 Which is best for my project and why [cite: 82]

[cite_start]For my project, **Microservices architecture** is the most suitable choice because: [cite: 83]

* [cite_start]**Matches assignment requirement:** I must implement microservices — so pick an approach that is practical to implement within time and demonstrates architectural concepts[cite: 84].
* [cite_start]**Balanced complexity:** Microservices give separation and scalability without the full complexity of an event-sourcing Kafka ecosystem[cite: 85]. [cite_start]**RabbitMQ + Spring Boot microservices** gives a manageable learning curve[cite: 86].
* [cite_start]**Good mapping to domain:** Each domain area (Users, Events, Bookings, Notifications, Rewards) becomes a natural independent service[cite: 87]. [cite_start]This maps to the course requirement to showcase design patterns across services (Observer via broker, Strategy in Reward Service, Builder in Event/Booking models)[cite: 88].
* [cite_start]**Incremental delivery:** you can start with a small set of services (User, Event, Booking, Notification, Reward) and expand [cite: 89][cite_start]; you can run locally with docker-compose + RabbitMQ (I provided a starter earlier)[cite: 90].
* [cite_start]**Academic depth & industry relevance:** Microservices let you demonstrate distributed design, inter-service communication patterns, and trade-offs (sagas, eventual consistency) — strong material for the report & evaluation[cite: 91].
