# Backend Interview Problem: Restaurant Top-Rated API

Build a small Spring Boot REST service to manage restaurant data.

## Restaurant Payload
Each restaurant has this shape:

```json
{
  "city": "Omaha",
  "name": "Aaira",
  "estimated_cost": 180,
  "user_rating": {
    "average_rating": 4.4,
    "votes": 861
  },
  "id": 211
}
```

## Requirements

### 1) Add Restaurant API
Implement:

- `POST /restaurants`

Behavior:
- Accept a restaurant JSON payload.
- Store it in memory (no database required).
- Return appropriate status code for success/failure.

### 2) Get Top Restaurant API
Implement:

- `GET /restaurants/top`

Optional query parameters:
- `city`
- `budget` (maximum allowed `estimated_cost`)

Selection rules:
- If only `city` is provided: return the highest-rated restaurant in that city.
- If only `budget` is provided: return the highest-rated restaurant across all cities with `estimated_cost <= budget`.
- If both `city` and `budget` are provided: return the highest-rated restaurant in that city with `estimated_cost <= budget`.
- If neither is provided: return the highest-rated restaurant overall.

If no restaurant matches the filter, return `404 Not Found`.

## Ranking / Tie-breakers
For "highest-rated", sort by:
1. Higher `user_rating.average_rating`
2. If equal, higher `user_rating.votes`
3. If equal, lower `estimated_cost`
4. If equal, lower `id`

## Constraints / Expectations
- Keep the solution simple and production-minded.
- Use clean layering (controller/service/model).
- Add basic input validation for required fields and obvious invalid values.
- Handle error responses cleanly.
- In-memory storage is sufficient for this exercise.

## Timebox
Aim to complete this in **45 minutes**.

## Nice-to-have (if time permits)
- Unit tests for ranking/filter logic.
- API contract documentation in README or comments.
