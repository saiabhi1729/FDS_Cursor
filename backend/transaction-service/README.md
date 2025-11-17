# Transaction Service

Reactive Spring Boot microservice responsible for ingesting card and alternative payment transactions, persisting them with PostgreSQL (R2DBC), and broadcasting Kafka events for downstream fraud detection, notification, and analytics services.

## Features
- **Reactive API** powered by Spring WebFlux with validation and RFC7807 error envelopes.
- **PostgreSQL persistence** using Spring Data R2DBC with auditing metadata and schema bootstrapping.
- **Kafka integration** publishing `TransactionCreatedEvent` messages to the `transactions.tx.created` topic.
- **Risk heuristics** to score transactions before they reach the AI fraud service, enabling instant blocking.
- **Observability ready** via Spring Boot Actuator and OpenAPI documentation via SpringDoc.

## Running Locally
1. Ensure infrastructure dependencies are available (PostgreSQL, Kafka). A `docker-compose` workspace will be added later.
2. Export environment variables if you need to override defaults:
   ```bash
   export SPRING_R2DBC_URL=r2dbc:postgresql://localhost:5432/transactions
   export SPRING_R2DBC_USERNAME=postgres
   export SPRING_R2DBC_PASSWORD=postgres
   export SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
   ```
3. Build and run tests:
   ```bash
   mvn verify
   ```
4. Start the service:
   ```bash
   mvn spring-boot:run
   ```
5. Swagger UI becomes available at [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html).

## Docker
Build and run the containerized service:
```bash
docker build -t transaction-service .
docker run -p 8081:8081 --env SPRING_R2DBC_URL=... --env SPRING_KAFKA_BOOTSTRAP_SERVERS=... transaction-service
```

## API Surface (high level)
- `POST /api/v1/transactions` — creates a transaction, runs risk scoring, stores it, and publishes a Kafka event.
- `GET /api/v1/transactions/{id}` — fetches a single transaction.
- `GET /api/v1/transactions?merchantId=...` — paginated query per merchant.
- `PATCH /api/v1/transactions/{id}/status` — updates workflow status (e.g., block/authorize).

## Next Steps
- Add asynchronous orchestration tests with Testcontainers for PostgreSQL & Kafka.
- Wire into the fraud-service consumer once implemented.
- Expand risk analyzer to call the dedicated AI microservice over REST/gRPC.
