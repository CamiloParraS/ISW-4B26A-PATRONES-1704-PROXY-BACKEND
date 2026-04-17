# SlopGPT Backend

SlopGPT is a Spring Boot backend that simulates an AI text-generation platform with a proxy-based policy chain.

It enforces:

- request-per-minute limits by subscription plan
- monthly token quotas by subscription plan
- JSON-only persistence for all backend state
- frontend-friendly API responses for status and history views

## Proxy chain

Every generation request follows this order:

`RateLimitProxyService -> QuotaProxyService -> MockAIGenerationService`

- Rate proxy rejects with `429` + `Retry-After` when minute limit is exceeded.
- Quota proxy rejects with `402` when monthly tokens are exhausted.
- Mock service sleeps 1200 ms and returns canned text.

## Plan limits

- `FREE`: 10 requests/minute, 50,000 tokens/month
- `PRO`: 60 requests/minute, 500,000 tokens/month
- `ENTERPRISE`: unlimited requests and tokens

## Running the backend

Requirements:

- Java 25
- Maven Wrapper (included)

Run:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Run tests:

```powershell
.\mvnw.cmd test
```

## JSON persistence

State file:

- `./data/slopgpt-state.json`

Configured in:

- `src/main/resources/application.properties`

Persisted data includes:

- user plan
- request counters
- monthly token usage
- daily quota history (last 7 days)
- upgrade history
- encrypted password received during registration

The file is written atomically through temp-file replacement and guarded by repository locks.

## Scheduled jobs

- every minute: rate-limit counters reset
- first day of month at 00:00: monthly quota reset

## Main endpoints

- `POST /api/ai/generate`
- `GET /api/quota/status`
- `GET /api/quota/history`
- `POST /api/quota/upgrade`
- `POST /api/users/register`

## Postman collection for QA

Collection file:

- `postman/SlopGPT-QA.postman_collection.json`

How to use:

- Open Postman and import the collection JSON file.
- Ensure `baseUrl` points to your running backend (default: `http://localhost:8080`).
- Run requests in order from 01 to 06 for standard flow validation.
- Use 07 and 08 for manual stress/error-path QA (`429` and `402`).

## Example requests

### Register user

```http
POST /api/users/register
Content-Type: application/json

{
  "userId": "camilo",
  "encryptedPassword": "$2b$12$PRE_ENCRYPTED_HASH"
}
```

### Generate text

```http
POST /api/ai/generate
Content-Type: application/json

{
  "userId": "camilo",
  "prompt": "Give me an API design checklist",
  "maxOutputTokens": 150
}
```

### Upgrade FREE -> PRO

```http
POST /api/quota/upgrade
Content-Type: application/json

{
  "userId": "camilo"
}
```

## Error format

All errors return:

- `message`
- `timestamp`
- `path`
- `details` (optional list)

Example:

```json
{
  "message": "Monthly token quota exhausted for your current plan. Upgrade your plan or wait until 2026-05-01.",
  "timestamp": "2026-04-17T22:16:40.130Z",
  "path": "/api/ai/generate",
  "details": []
}
```

## More details

Full architecture and API contracts are documented in:

- `docs/ARCHITECTURE.md`
