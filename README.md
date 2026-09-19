# Journal

A small journaling REST service built with Spring Boot (Java 17).

## Overview

This project provides a simple journaling backend with authentication, sentiment-related utilities, and integrations for caching, messaging, and email notifications.

Key implementations:
- MongoDB (data persistence via Spring Data MongoDB)
- Redis (caching via Spring Data Redis)
- Kafka (publish/consume sentiment messages via Spring Kafka)
- Spring Security with JWT (stateless auth, BCrypt password hashing)
- Email notifications (Spring Mail)
- Scheduled jobs (Spring scheduling for weekly summaries and cache refresh)
- External API integration (weather info via RestTemplate)

## Tech stack

- Java 17
- Spring Boot 3.4.x
- Spring Data MongoDB
- Spring Data Redis
- Spring Security
- Spring Kafka
- JJWT (io.jsonwebtoken)
- Lombok

## Configuration & Environment

Configuration lives in `src/main/resources/application.yml` and supports environment variable substitution.

Important environment variables (examples):

- `MONGO_URI` — MongoDB connection URI (e.g. `mongodb://localhost:27017/journaldb`)
- `REDIS_URI` — Redis connection URL (e.g. `redis://localhost:6379`)
- `GMAIL_USERNAME` — Gmail username used by Spring Mail
- `GMAIL_PASSWORD` — Gmail app password
- Kafka is configured via `spring.kafka.bootstrap-servers` (default `localhost:9092` in `application.yml`)

Note: The project currently has a hardcoded JWT secret inside `JwtUtil` — for production, externalize secrets and rotate them.

## Build & Run

Build with Maven:

```bash
mvn clean package
```

Run locally:

```bash
mvn spring-boot:run
# or
java -jar target/Journal-0.0.1-SNAPSHOT.jar
```

Docker (image is supported by the included `Dockerfile`):

```bash
docker build -t journal-app .
docker run -e MONGO_URI=... -e REDIS_URI=... -e GMAIL_USERNAME=... -e GMAIL_PASSWORD=... -p 8081:8081 journal-app
```

## Main features & components

- Authentication: `/public/signup` and `/public/login` endpoints. Login returns a JWT (use `Authorization: Bearer <token>` for protected endpoints).
- Roles: `USER` and `ADMIN`. Security configuration in `SpringSecurity` restricts `/admin/**` to admins and `/journal/**` & `/user/**` to authenticated users.
- Journal entries: CRUD endpoints under `/journal` (see `JournalEntryControllerV2`). Journal entries are stored in MongoDB.
- Caching: Weather responses are cached in Redis by `WeatherService` using `RedisService` and `AppCache`.
- Kafka: `UserScheduler` publishes weekly sentiment messages to `weekly.sentiment`. `SentimentConsumerService` listens on this topic and sends emails.
- Email: `EmailService` sends plain text emails using Spring Mail (configured in `application.yml`).
- Scheduling: `UserScheduler` contains scheduled jobs to compute the weekly sentiment and refresh caches.

## Endpoints (summary)

- `GET /public/health-check` — service health
- `POST /public/signup` — register new user (no auth)
- `POST /public/login` — authenticate and receive JWT
- `GET /public/weather` — weather info (uses cache)
- `POST /public/kafka-publish` — publish sentiment message to Kafka (public helper)
- `GET /public/cache-refresh` — refresh application cache
- `GET /journal/{userName}` — list journal entries for a user (authenticated)
- `POST /journal/{userName}` — create journal entry (authenticated)
- `PUT /journal/id/{userName}/{myId}` — update entry by id
- `GET /journal/id/{id}` — get entry by id
- `DELETE /journal/id/{userName}/{id}` — delete entry

Protected routes: `/journal/**` and `/user/**` require a valid JWT in `Authorization` header.

## Useful files

- `src/main/resources/application.yml` — main configuration
- `src/main/java/.../config/RedisConfig.java` — Redis template configuration
- `src/main/java/.../config/SpringSecurity.java` — security setup
- `src/main/java/.../filters/JwtFilter.java` — JWT authentication filter
- `src/main/java/.../scheduler/UserScheduler.java` — scheduled jobs for sentiment
- `src/main/java/.../service/KafkaPublishService.java` and `SentimentConsumerService.java` — Kafka producer/consumer

## License
MIT
