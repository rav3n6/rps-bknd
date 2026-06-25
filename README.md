# Rock-Paper-Scissors Backend

## Project Purpose
This is a Spring Boot REST API for a Rock-Paper-Scissors game. It provides endpoints to play a round, fetch game history, retrieve game statistics, and reset the game state.

## Technology Stack
* Java 17
* Spring Boot 4.1.0
* Spring Web
* Spring Data JPA
* H2 Database
* Jakarta Bean Validation
* Maven

## Prerequisites
* Java 17+
* Maven 3.8+ (or use the provided Maven wrapper)

## How to Run the Application
1. Clone the repository.
2. Navigate to the project root.
3. Run the application using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
   The API will be available at `http://localhost:8080`.

## How to Run Tests
Execute the automated test suite using:
```bash
./mvnw test
```

## Database Details
* **Development Database:** Uses a file-based H2 database to persist game data across application restarts. The database files are stored in the `./data` directory (excluded via `.gitignore`).
* **Test Database:** Uses an isolated, in-memory H2 database (`jdbc:h2:mem:rpstestdb`) to ensure tests do not affect real game data.
* **H2 Console URL:** `http://localhost:8080/h2-console`
* **H2 JDBC URL (Local Dev):** `jdbc:h2:file:./data/rpsdb`
* **Username:** `sa`
* **Password:** (blank)

## Architecture Decisions
* **Layered Architecture:** The application separates routing/controllers, business logic/services, data access/repositories, and models/entities.
* **Constructor Injection:** Ensures dependency immutability and testability.
* **Random Move Generation:** Extracted to an interface (`ComputerMoveGenerator`) to allow predictable moves during unit testing.
* **Game Rules Logic:** Segregated into a dedicated `GameRules` component to decouple complex game evaluation logic from the service layer.
* **CORS:** Configured centralized in `WebConfig` to permit interactions from the default Angular host `http://localhost:4200`.

## API Endpoints

| Method | Endpoint          | Description                                  |
|--------|-------------------|----------------------------------------------|
| POST   | `/api/rounds`     | Play a round against the computer.           |
| GET    | `/api/rounds`     | Retrieve a history of all rounds played.     |
| GET    | `/api/statistics` | Retrieve game statistics (wins, losses, etc.)|
| DELETE | `/api/rounds`     | Clear the entire game history.               |

## Example Requests & Responses

### 1. Play a Round
**Request:**
```bash
curl -X POST http://localhost:8080/api/rounds \
  -H "Content-Type: application/json" \
  -d '{"playerMove":"ROCK"}'
```
**Response (201 Created):**
```json
{
  "id": 1,
  "playerMove": "ROCK",
  "computerMove": "SCISSORS",
  "result": "WIN",
  "playedAt": "2023-10-27T10:00:00Z"
}
```

### 2. Get History
**Request:**
```bash
curl http://localhost:8080/api/rounds
```
**Response (200 OK):**
```json
[
  {
    "id": 1,
    "playerMove": "ROCK",
    "computerMove": "SCISSORS",
    "result": "WIN",
    "playedAt": "2023-10-27T10:00:00Z"
  }
]
```

### 3. Get Statistics
**Request:**
```bash
curl http://localhost:8080/api/statistics
```
**Response (200 OK):**
```json
{
  "totalRounds": 1,
  "wins": 1,
  "losses": 0,
  "draws": 0,
  "winRate": 100.0
}
```

### 4. Reset Game
**Request:**
```bash
curl -X DELETE http://localhost:8080/api/rounds
```
**Response (204 No Content):** No body returned.
