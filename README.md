# Order Engine

A side project for learning purposes — a barter-style order matching engine built with Spring Boot. Users place orders to exchange one product for another, and the engine matches compatible orders asynchronously.

## How it works

When a user places an order (e.g. "sell 2 ETH, want 1 BTC"), the engine looks for a compatible counterpart order (e.g. "sell 1 BTC, want 2 ETH or less"). Matching happens asynchronously via an in-memory order book backed by a `BlockingQueue` and a single consumer thread. On application startup, all unresolved orders are loaded from the database and restored to the order book.

Two orders match when:
- They trade the same product pair in opposite directions
- The incoming order's `buyAmount` ≤ the counterpart's `sellAmount`
- The incoming order's `sellAmount` ≥ the counterpart's `buyAmount`

When a match is found, both orders are marked as `RESOLVED` in the database within a single transaction.

## Tech stack

- Java 21
- Spring Boot 4
- Spring Security with JWT authentication (OAuth2 Resource Server)
- Spring Data JPA + Hibernate
- PostgreSQL
- Flyway (database migrations)
- Docker / Docker Compose

## Getting started

### Prerequisites

- Docker and Docker Compose

### Setup

1. Clone the repository
2. Copy `.env.example` to `.env` and fill in the values:
   ```
   cp .env.example .env
   ```
3. Start the application:
   ```
   docker compose up --build
   ```

The API will be available at `http://localhost:8080`.

## API

All endpoints except `/api/register` and `/api/login` require a Bearer token in the `Authorization` header.

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/register` | Register a new user |
| POST | `/api/login` | Login and receive access + refresh tokens |
| POST | `/api/refresh` | Refresh an expired access token |

### Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | List all available products |

### Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/orders` | List all orders for the authenticated user |
| GET | `/api/orders/{id}` | Get a specific order |
| POST | `/api/orders` | Place a new order |
| DELETE | `/api/orders/{id}` | Cancel an order |

#### Place order request body

```json
{
  "sellProductId": 1,
  "sellAmount": 2,
  "buyProductId": 2,
  "buyAmount": 1
}
```

#### Order status

Orders are returned with a `status` field:
- `NEW` — waiting to be matched
- `RESOLVED` — matched with a counterpart order
