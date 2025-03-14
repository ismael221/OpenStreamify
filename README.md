# OpenStreamify - Movie Streaming Application

[![Platform](https://img.shields.io/badge/platform-Web-orange.svg)](https://spring.io/projects/spring-boot)
[![Languages](https://img.shields.io/badge/language-Java-orange.svg)](https://www.java.com/)
[![Framework](https://img.shields.io/badge/framework-Spring%20Boot-green.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-compatible-blue.svg)](https://www.docker.com/)
[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://github.com/ismael221/OpenStreamify/releases)


## Description

This is a self-hosted video streaming platform developed in **Spring Boot** for video and series management and streaming. The application includes features like JWT-based authentication, OAuth2 login, **Spring MVC** for handling HTTP requests, and an access control system based on user permissions. Additionally, it supports video and series streaming using **HLS (HTTP Live Streaming)**, notifications when a new video or series is added, and a one-time password (OTP) system for secure password recovery.

### Features

- **User Authentication**:
    - JWT-based authentication for secure API access.
    - OAuth2 login options (Google, GitHub) for simplified access.
- **Movie and Series Management**:
    - Create, update, delete, and list movies and series with role-based access control.
    - Notifications for users when new movies or series are added.
- **Streaming**:
    - Video streaming in HLS format for both movies and series.
- **Password Recovery**:
    - OTP system for secure password recovery through email.
- **Caching and Messaging**:
    - **Redis** for caching frequently accessed data, improving response times.
    - **RabbitMQ** for asynchronous messaging, supporting high-scale processing.
- **Storage and Monitoring**:
    - **minIO** for video file storage.
    - **Grafana and Prometheus** for system monitoring and performance tracking.

## Technologies Used

- **Java**: Main programming language.
- **Spring Boot**: Framework used for developing the application.
- **Spring Security**: For authentication and authorization using JWT.
- **JWT (JSON Web Token)**: For secure API authentication.
- **Spring MVC**: For managing HTTP requests and routing.
- **HLS (HTTP Live Streaming)**: For video streaming.
- **Thymeleaf**: Template engine to render HTML pages.
- **ModelMapper**: For entity-to-DTO conversion.
- **Docker**: For containerizing the application and monitoring services.
- **PostgreSQL**: Relational database used for storing movies and users data.
- **Redis**: Caching system for optimizing queries.
- **RabbitMQ**: Messaging system for inter-service communication.
- **Grafana**: Monitoring and analytics platform.
- **Prometheus**: Monitoring and alerting toolkit.
- **minIO**: Object storage used to store video files.

## System Requirements

- **JDK 17** or later
- **Maven** 3.6+
- **Docker** (Recommended for running the application and dependencies)

## Setup and Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/ismael221/OpenStreamify
   ```

2. Create a `.env` file in the root directory and add the following environment variables:

   ```env
   # 🔒 Security
   JWT_SECRET=your_jwt_secret_here
   
   # 🌐 Server Settings
   SERVER_URL=http://localhost:8080
   SERVER_PORT=8080
   
   # 🛢️ Database
    POSTGRESQL_PASSWORD=your_postgres_password_here
    POSTGRESQL_USER=postgres
    POSTGRESQL_DATABASE=your_database_name_here
   
   # 🔴 Redis
   REDIS_HOST=localhost
   REDIS_PORT=6379
   REDIS_TIME_TO_LIVE=1h
   
   # 🐇 RabbitMQ
   RABBIT_HOST=localhost
   RABBIT_PORT=5672
   RABBIT_USERNAME=guest
   RABBIT_PASSWORD=guest
   
   # 📦 MinIO
   MINIO_ENDPOINT=http://localhost:9000
   MINIO_ACCESSKEY=your_minio_access_key_here
   MINIO_SECRETKEY=your_minio_secret_key_here
   MINIO_BUCKET=your_minio_bucket_name_here
   
   # 📧 SMTP (Email)
   SMTP_HOST=smtp.example.com
   SMTP_PORT=587
   SMTP_USER=your_email@example.com
   SMTP_PASSWORD=your_email_password_here
   
   # 📢 Telegram Bot
   TELEGRAM_BOT_TOKEN=your_telegram_bot_token_here
   TELEGRAM_CHAT_ID=your_telegram_chat_id_here
   
   # 🔑 OAuth2 - GitHub
   GITHUB_CLIENTID=your_github_client_id_here
   GITHUB_CLIENTSECRET=your_github_client_secret_here
   
   # 🔑 OAuth2 - Google
   GOOGLE_CLIENTID=your_google_client_id_here
   GOOGLE_CLIENTSECRET=your_google_client_secret_here
   ```

3. Build and run the application using Docker:
   ```bash
   docker-compose up -d --build
   ```

4. Access the application in your browser:
   ```bash
   http://localhost:8080
   ```

5. Access Grafana for monitoring:
   ```bash
   http://localhost:3000
   ```
    - Username: `admin`
    - Password: `admin`

6. Access Prometheus to view collected data:
   ```bash
   http://localhost:9090
   ```

## Main Endpoints

### Authentication

- **POST** `/auth/register`: User registration.
- **POST** `/auth/login`: User authentication and JWT generation.

### Movies

- **GET** `/api/v1/movies`: Lists all movies.
- **POST** `/api/v1/movies`: Adds a new video.
- **GET** `/api/v1/movies/{rid_movie}`: Retrieves details of a specific video.
- **POST** `/api/v1/ratings`: Adds a review for a video.

### Streaming

- **GET** `/api/v1/media/hls/{filename}.m3u8`: Streams the video using HLS based on the file name.

## Swagger UI

To see all available endpoints and their descriptions, access Swagger UI:

http://localhost:8080/swagger-ui.html

## Contributing

Contributions are welcome! Feel free to open an **issue** or submit a **pull request**.

## License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for more details.

