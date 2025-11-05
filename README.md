# URL Shortener API - MVP

This project is a **minimal viable product (MVP)** of a URL shortening application built with **Kotlin** and **Spring Boot**, using **MongoDB** as the database. It allows users to shorten URLs, retrieve original URLs, track clicks, update, and deactivate shortened URLs.

---

## Features

* Shorten any valid URL with an auto-generated short code.
* Retrieve the original URL by short code.
* Increment click count each time a shortened URL is accessed.
* Update URL or description.
* Deactivate URLs manually.
* Basic input validation and error handling.
* MongoDB persistence.

---

## Technologies

* Kotlin 2.2.x
* Spring Boot 3.3.x
* Spring Data MongoDB
* JUnit 5 (for testing)
* dotenv for environment variables

---

## Getting Started

### Prerequisites

* Java 21
* MongoDB Atlas or local MongoDB instance
* Gradle 8.x

### Environment Variables

Create a `.env` file at the project root:

```dotenv
MONGO_USER=<your_mongo_user>
MONGO_PASSWORD=<your_mongo_password>
```

### Run the Application

```bash
./gradlew bootRun
```

Application runs by default on port `8080`.

---

## API Endpoints

### Health Check

```http
GET /api/url/
```

**Response:**

```json
{
  "message": "URL Shortener API is running!"
}
```

---

### Shorten URL

```http
POST /api/url/shorten
```

**Parameters:**

* `url` (required): The original URL.
* `description` (optional): Description of the URL.
* `X-Forwarded-For` (optional): Client IP.

**Response:**

```json
{
  "shortCode": "abcd1234",
  "originalUrl": "https://example.com",
  "createdAt": "2025-11-03T14:21:30.123Z",
  "description": "Example site",
  "clicks": 0,
  "active": true
}
```

---

### Resolve URL

```http
GET /api/url/{code}
```

**Response if found:**

```json
{
  "originalUrl": "https://example.com"
}
```

**Response if not found:**

```json
{
  "error": "URL not found or expired"
}
```

---

### Update URL

```http
PUT /api/url/{code}/update
```

**Parameters:**

* `newUrl` (optional)
* `newDescription` (optional)

**Response:** Returns updated URL object or error if not found.

---

### Delete URL

```http
DELETE /api/url/{code}
```

**Response:**

```json
{
  "message": "URL deleted successfully"
}
```

or

```json
{
  "error": "URL not found"
}
```

---

### Deactivate URL

```http
PUT /api/url/{code}/deactivate
```

**Response:** Success or error message.

---

### Get All URLs

```http
GET /api/url/all
```

**Response:** List of all URL objects in the system.

---

## Validation & Error Handling

* Only valid URLs are accepted.
* Errors return proper HTTP statuses:

  * `400 Bad Request` for invalid URL.
  * `404 Not Found` for missing resources.
* Global error handling via `UrlApiExceptionHandler`.

---

## Notes

* Short codes are generated with 8 characters.
* The `clicks` count increments automatically on access.
* MongoDB connection uses credentials from `.env` file.

---

## License

MIT License
