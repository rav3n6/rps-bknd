# Rock Paper Scissors API

Spring Boot REST API for a single-player Rock Paper Scissors game. The API generates the computer's move, evaluates the round, stores the result, exposes the complete game history, calculates aggregate statistics, and supports resetting the session.

The companion Angular application is available in [`rav3n6/rps-ui`](https://github.com/rav3n6/rps-ui).

## Features

- Play Rock, Paper, or Scissors against a randomly generated computer move
- Persist completed rounds in a file-based H2 database
- Return game history in reverse chronological order
- Calculate wins, losses, draws, total rounds, and player win rate
- Clear all saved rounds through a reset endpoint
- Validate missing or invalid moves and return structured `400 Bad Request` responses
- Allow local requests from the Angular development server at `http://localhost:4200`

## Technology stack

- Java 17
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- Jakarta Bean Validation
- H2 Database
- Lombok
- Maven Wrapper
- JUnit 5 and Mockito

## Prerequisites

- JDK 17 or later

A separate Maven installation is not required because the repository includes the Maven Wrapper.

## Run locally

Clone the repository and enter its directory:

```bash
git clone https://github.com/rav3n6/rps-bknd.git
cd rps-bknd
```

Start the application on macOS or Linux:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
mvnw.cmd spring-boot:run
```

The API is served at:

```text
http://localhost:8080
```

## API reference

| Method | Endpoint | Success status | Description |
|---|---|---:|---|
| `POST` | `/api/rounds` | `201 Created` | Play and persist a new round |
| `GET` | `/api/rounds` | `200 OK` | Return all rounds, newest first |
| `GET` | `/api/statistics` | `200 OK` | Return aggregate game statistics |
| `DELETE` | `/api/rounds` | `204 No Content` | Delete the complete game history |

Valid move values are `ROCK`, `PAPER`, and `SCISSORS`.

### Play a round

```bash
curl -X POST http://localhost:8080/api/rounds \
  -H "Content-Type: application/json" \
  -d '{"playerMove":"ROCK"}'
```

Example response:

```json
{
  "id": 1,
  "playerMove": "ROCK",
  "computerMove": "SCISSORS",
  "result": "WIN",
  "playedAt": "2026-06-25T12:00:00Z"
}
```

The `result` value is always one of `WIN`, `LOSS`, or `DRAW`, from the player's perspective.

### Get round history

```bash
curl http://localhost:8080/api/rounds
```

Example response:

```json
[
  {
    "id": 1,
    "playerMove": "ROCK",
    "computerMove": "SCISSORS",
    "result": "WIN",
    "playedAt": "2026-06-25T12:00:00Z"
  }
]
```

### Get statistics

```bash
curl http://localhost:8080/api/statistics
```

Example response:

```json
{
  "totalRounds": 1,
  "wins": 1,
  "losses": 0,
  "draws": 0,
  "winRate": 100.0
}
```

`winRate` is expressed as a percentage and rounded to two decimal places.

### Reset the game

```bash
curl -X DELETE http://localhost:8080/api/rounds
```

A successful reset returns `204 No Content` with no response body.

## Validation errors

A missing move returns `400 Bad Request`:

```json
{
  "timestamp": "2026-06-25T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "playerMove must be provided",
  "path": "/api/rounds"
}
```

An unsupported move returns the message:

```text
Invalid move. Allowed values are ROCK, PAPER and SCISSORS
```

## Database

Development uses a file-based H2 database so game history survives application restarts.

| Setting | Value |
|---|---|
| JDBC URL | `jdbc:h2:file:./data/rpsdb` |
| Username | `sa` |
| Password | blank |
| Schema strategy | `update` |
| H2 console | `http://localhost:8080/h2-console` |

Database files are created under `./data` and excluded from version control.

Tests use the `test` Spring profile and an isolated in-memory database:

```text
jdbc:h2:mem:rpstestdb
```

## Run tests

On macOS or Linux:

```bash
./mvnw test
```

On Windows:

```powershell
mvnw.cmd test
```

The suite includes:

- All nine Rock Paper Scissors rule combinations
- Service behavior for round creation, history, statistics, and reset
- MVC endpoint status and response checks
- Validation checks for missing and invalid moves
- Spring application-context startup using the isolated test database

## Architecture

The backend follows a layered design:

```text
controller  -> HTTP endpoints and request validation
service     -> game rules, orchestration, and statistics
repository  -> Spring Data JPA persistence
entity      -> database model
dto         -> API request and response contracts
model       -> move and result enums
config      -> local CORS configuration
exception   -> structured API error handling
```

`ComputerMoveGenerator` is represented by an interface and implemented by `RandomComputerMoveGenerator`, keeping random move generation replaceable and independently testable. Game outcome evaluation is isolated in `GameRules`.

## Frontend integration

During local development, start this backend on port `8080`, then start the companion Angular UI on port `4200`. The frontend proxies `/api` requests to this service, while the backend also permits the Angular development origin through CORS.
