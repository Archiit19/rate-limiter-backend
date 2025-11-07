# Rate Limiter Backend (Token Bucket & Leaky Bucket)

### A simple Spring Boot backend showcasing two popular rate-limiting algorithms:

**Token Bucket** — allows bursts, refills tokens over time.

**Leaky Bucket** — maintains a steady flow and drops excess requests.

Switch dynamically between algorithms with a single API parameter!

### Overview

This backend exposes one test endpoint:

GET /rate/test


**You can specify:**

* Which algorithm to use (token or leaky)
* Bucket capacity
* Request rate per second
* Client ID (so each client has its own bucket)

### Tech Stack

1. Java 17+
2. Spring Boot 3.3.x
3. Maven 3.9+
4. REST API tested with curl / Postman

### Project Structure

rate-limiter-backend/
├── pom.xml
└── src/main/java/com/example/ratelimiter/
├── RateLimiterApplication.java
├── controller/RateLimiterController.java
├── model/Decision.java
└── service/
├── TokenBucketLimiter.java
└── LeakyBucketLimiter.java

###  Run Locally

1️⃣ Build the project
mvn clean package

2️⃣ Start the application
java -jar target/rate-limiter-backend-0.0.1-SNAPSHOT.jar


App will start on:

http://localhost:8080
