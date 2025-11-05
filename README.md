# 🧩 URL Shortener – Kotlin + Spring Boot + MongoDB + Docker

A simple and production-ready **URL Shortener API** built with **Kotlin**, **Spring Boot 3**, and **MongoDB**, containerized with **Docker Compose**.
This MVP provides endpoints for creating, retrieving, updating, deactivating, and deleting shortened URLs, with click tracking and optional descriptions.

---

## 🚀 Features

* ✨ Shorten any valid URL with a generated short code
* 📋 Retrieve original URLs using short codes
* 🛠 Update or deactivate existing links
* 📊 Track number of clicks
* 🧱 Fully containerized (App + MongoDB) with Docker Compose
* 🔒 Environment variable management via `.env`

---

## 🗂 Project Structure

```
url-shortener/
├── api/                     # REST controllers
│   └── UrlController.kt
├── application/              # Business logic layer
│   └── UrlService.kt
├── domain/                   # Domain models & validation
│   └── Url.kt
│   └── validation/UrlValidator.kt
├── infraestructure/          # MongoDB repository
│   └── repository/UrlRepository.kt
├── src/main/resources/       # Spring configuration files
├── src/test/kotlin/api/      # Controller tests (JUnit + Mockito)
│   └── UrlControllerTest.kt
├── Dockerfile
├── docker-compose.yml
├── build.gradle.kts
├── settings.gradle.kts
└── .env
```

---

## ⚙️ Environment Variables

Your `.env` file (in the project root) should look like this:

```env
MONGO_USER=admin
MONGO_PASSWORD=secret
MONGO_DB=url_shortener
MONGO_HOST=mongo
MONGO_PORT=27017
```

These variables are automatically loaded by Spring Boot when the container starts.

---

## 🐳 Docker Deployment

### 1️⃣ Build and run with Docker Compose

```bash
docker compose up --build
```

This will:

* Build the Kotlin Spring Boot app
* Launch a MongoDB container
* Start both with proper networking and environment variables

Once up, visit:

```
http://localhost:8080/api/url/
```

You should see:

```json
{
  "message": "URL Shortener API is running!"
}
```

---

### 2️⃣ Stop the containers

```bash
docker compose down
```

---

## 🧠 API Endpoints

| Method   | Endpoint                                        | Description                              |
| -------- | ----------------------------------------------- | ---------------------------------------- |
| `GET`    | `/api/url/`                                     | Health check endpoint                    |
| `POST`   | `/api/url/shorten?url={url}&description={desc}` | Create a shortened URL                   |
| `GET`    | `/api/url/{code}`                               | Resolve a short code to its original URL |
| `PUT`    | `/api/url/{code}/update`                        | Update the original URL or description   |
| `PUT`    | `/api/url/{code}/deactivate`                    | Deactivate a link                        |
| `DELETE` | `/api/url/{code}`                               | Delete a link                            |
| `GET`    | `/api/url/all`                                  | Retrieve all URLs                        |

---

## 🧪 Running Tests

```bash
./gradlew test
```

Tests are written with **JUnit 5** and **Mockito Kotlin**.
To run them inside Docker, use:

```bash
docker compose run app ./gradlew test
```

---

## 🧱 Build Jar Locally (optional)

If you want to build and run without Docker:

```bash
./gradlew clean build -x test
java -jar build/libs/url-shortener-0.0.1-SNAPSHOT.jar
```

---

## 📜 License

MIT License © 2025 — Developed by Alejandro García.
Use freely for learning, prototyping, or building your own link shortener 🚀
