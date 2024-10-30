
---

# OpenStreamify - Movie Streaming Application

## Description

This is a web application developed in **Spring Boot** for movie and series management and streaming. The application includes features like JWT-based authentication, OAuth2 login, **Spring MVC** for handling HTTP requests, and an access control system based on user permissions. Additionally, it supports movie and series streaming using **HLS (HTTP Live Streaming)**, notifications when a new movie or series is added, and a one-time password (OTP) system for secure password recovery.

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
- **MySQL**: Relational database used for storing movies and users data.
- **Redis**: Caching system for optimizing queries.
- **RabbitMQ**: Messaging system for inter-service communication.
- **Grafana**: Monitoring and analytics platform.
- **Prometheus**: Monitoring and alerting toolkit.
- **minIO**: Object storage used to store movie files.



## System Requirements

- **JDK 17** or later
- **Maven** 3.6+
- **MySQL** or any other relational database
- **Redis** (optional, but recommended for caching)
- **Docker** (to run RabbitMQ, Grafana, and Prometheus)
- **Postman** (to test the API endpoints)
- **FFmpeg** (To convert videos into .m3u8 and .ts segments)
- **minIO** (to store video files)

---

## Setup and Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/ismael221/OpenStreamify
   ```

2. Update your `application.yml` with the following configurations:

```yaml
spring:
  output:
    ansi:
      enabled: ALWAYS
  datasource:
    username: your db user
    password: your db password
    url: jdbc:mysql://localhost:3306/yourDatabase
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true
    database-platform: org.hibernate.dialect.MySQLDialect
    hibernate:
      ddl-auto: update
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
  servlet:
    multipart:
      enabled: true
      max-file-size: 6048MB
      max-request-size: 6048MB
  mail:
    host: ${SMTP_HOST}
    port: ${SMTP_PORT}
    username: ${SMTP_USER}
    password: ${SMTP_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
  redis:
    host: ${REDIS_HOST}
    port: ${REDIS_PORT}
    time-to-live: 1h
  rabbitmq:
    host: ${RABBIT_HOST}
    port: ${RABBIT_PORT}
    username: ${RABBIT_USERNAME}
    password: ${RABBIT_PASSWORD}
  resources:
    static-locations: file:videos/hls/
  security:
    oauth2:
      client:
        registration:
          github:
            client-id: ${GITHUB_CLIENTID}
            client-secret: ${GITHUB_CLIENTSECRET}
            scope:
              - user:email
              - user
          google:
            client-id: ${GOOGLE_CLIENTID}
            client-secret: ${GOOGLE_CLIENTSECRET}
            scope:
              - profile
              - email
api:
  security:
    token:
      secret: ${JWT_SECRET}

management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint:
    prometheus:
      enabled: true

logging:
  level:
    root: DEBUG
    com.ismael.movie: ERROR
    org.hibernate.SQL: ERROR
    org.springframework.web: DEBUG

server:
  tomcat:
    max-swallow-size: -1
  url: ${SERVER_URL}
  port: ${SERVER_PORT}

minio:
  endpoint: ${MINIO_ENDPOINT}
  access-key: ${MINIO_ACCESSKEY}
  secret-key: ${MINIO_SECRETKEY}
  bucket:
    stream: ${MINIO_BUCKET}

TELEGRAM_BOT_TOKEN: ${TELEGRAM_BOT_TOKEN}
TELEGRAM_CHAT_ID: ${TELEGRAM_CHAT_ID}
```

### Environment Variables

Make sure to set up the following environment variables in your system or in a `.env` file:

- `SMTP_HOST`: SMTP server for email.
- `SMTP_PORT`: Port for SMTP.
- `SMTP_USER`: Username for SMTP authentication.
- `SMTP_PASSWORD`: Password for SMTP authentication.
- `REDIS_HOST`: Host for Redis server.
- `REDIS_PORT`: Port for Redis server.
- `RABBIT_HOST`: Host for RabbitMQ.
- `RABBIT_PORT`: Port for RabbitMQ.
- `RABBIT_USERNAME`: Username for RabbitMQ.
- `RABBIT_PASSWORD`: Password for RabbitMQ.
- `GITHUB_CLIENTID`: OAuth client ID for GitHub.
- `GITHUB_CLIENTSECRET`: OAuth client secret for GitHub.
- `GOOGLE_CLIENTID`: OAuth client ID for Google.
- `GOOGLE_CLIENTSECRET`: OAuth client secret for Google.
- `JWT_SECRET`: Secret key for JWT token encryption.
- `SERVER_URL`: Base URL for the server.
- `SERVER_PORT`: Port on which the server will run.
- `MINIO_ENDPOINT`: URL for minIO.
- `MINIO_ACCESSKEY`: Access key for minIO.
- `MINIO_SECRETKEY`: Secret key for minIO.
- `MINIO_BUCKET`: Bucket name for video storage in minIO.
- `TELEGRAM_BOT_TOKEN`: Token for Telegram bot.
- `TELEGRAM_CHAT_ID`: Chat ID for Telegram notifications
   

3. Start Redis (if using Docker):
   ```bash
   docker run -d --name redis -p 6379:6379 redis
   ```

4. Start RabbitMQ with the following command:
   ```bash
   docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management
   ```

5. Configure **minIO** for storing video files. You can follow [this tutorial](https://www.digitalocean.com/community/tutorials/how-to-set-up-minio-object-storage-server-in-standalone-mode-on-ubuntu-20-04) to set up **minIO** on Ubuntu. For example, to start **minIO** on Docker:
   ```bash
   docker run -d -p 9000:9000 --name minio \
   -e "MINIO_ACCESS_KEY=your_access_key" \
   -e "MINIO_SECRET_KEY=your_secret_key" \
   minio/minio server /data
   ```

6. Run the project with the following command in the root directory:
   ```bash
   mvn spring-boot:run
   ```

7. Start Grafana and Prometheus in Docker containers by running:
   ```bash
   docker-compose up -d
   ```

8. Access the application in your browser:
   ```bash
   http://localhost:8080
   ```

9. Access Grafana for monitoring:
   ```bash
   http://localhost:3000
   ```
   - Username: `admin`
   - Password: `admin`

10. Access Prometheus to view collected data:
   ```bash
   http://localhost:9090
   ```

## Main Endpoints

### Authentication

- **POST** `/auth/register`: User registration.
- **POST** `/auth/login`: User authentication and JWT generation.

### Movies

- **GET** `/api/v1/movies`: Lists all movies.
- **POST** `/api/v1/movies`: Adds a new movie.
- **GET** `/api/v1/movies/{rid_movie}`: Retrieves details of a specific movie.
- **POST** `/api/v1/ratings`: Adds a review for a movie.

### Streaming

- **GET** `/api/v1/media/hls/{filename}.m3u8`: Streams the video using HLS based on the file name.


### Example of an Authenticated Request with JWT and OAuth2

All routes, except for login and registration, require authentication. The application supports two methods for authenticated requests:

1. **JWT Authentication**: Users can log in with username and password, receiving a JWT token in the response, which is stored as an HTTP-only cookie.
2. **OAuth2 Authentication**: Users can log in via OAuth2 providers (such as GitHub or Google), which returns an authentication cookie upon successful login.

#### JWT Authentication

When logging in with JWT, the server responds with a cookie named `access_token`, containing the JWT token. This cookie will be automatically sent with each request to protected routes.

To access protected routes with JWT authentication, ensure that the `access_token` cookie is included in your request headers. Alternatively, you may manually include the JWT token in the `Authorization` header as shown below.

### Login Request (JWT)

```http
POST /api/login
Content-Type: application/json

{
  "username": "your-username",
  "password": "your-password"
}
```

### Swagger UI

To see all available endpoints and their descriptions, access Swagger UI:

http://localhost:8080/swagger-ui.html


## Project Structure

- `src/main/java/com/ismael/movies`: Contains the Java classes, including controllers, services, models, and repositories.
- `src/main/resources/templates`: Contains HTML pages rendered by Thymeleaf.
- `src/main/resources/static`: Contains static files like CSS and JavaScript.
- `src/main/resources/application.properties`: Application configuration.

## Security

The application uses **JWT tokens** and **OAuth2** for authentication and authorization. After logging in, the user receives a token or/and a cookie that must be included in the header of all subsequent requests to protected routes.

## Contributing

Contributions are welcome! Feel free to open an **issue** or submit a **pull request**.

## License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for more details.

---