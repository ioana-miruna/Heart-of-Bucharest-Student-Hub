# Heart of Bucharest — Microservices POC

This project contains three microservices:
- user-service (port 8081)
- event-service (port 8082)
- booking-service (port 8083)

They communicate via REST and RabbitMQ (message queue).

## Requirements
- Docker & Docker Compose
- Java 17 & Maven (if you prefer building locally)

## Build & Run (Docker Compose)
1. In the project root (where `docker-compose.yml` is), run: docker-compose up --build

2. Wait until services are up. RabbitMQ UI is at http://localhost:15672 (guest/guest).

## Endpoints (Postman collection included)
- Create user: `POST http://localhost:8081/api/users` — body: `{"name":"Alex","email":"alex@example.com"}`
- Get user: `GET http://localhost:8081/api/users/{id}`
- Create event: `POST http://localhost:8082/api/events` — body: `{"name":"Title","location":"Loc","isFree":true,"capacity":10}`
- Get event: `GET http://localhost:8082/api/events/{id}`
- Create booking: `POST http://localhost:8083/api/bookings` — body: `{"customerId":1,"eventId":1}`
- Get user points: `GET http://localhost:8081/api/users/{id}/points`

## Testing
1. Import `HeartOfBucharest.postman_collection.json` into Postman.
2. Create a user, create an event, then create a booking.
3. After booking, check user points (user-service consumes the booking.created message and updates points).

