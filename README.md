
---

# OpenStreamify - Movie Streaming Application

## Description

This is a web application developed in **Spring Boot** for movie management and streaming. The application includes features like JWT-based authentication, **Spring MVC** for handling HTTP requests, and an access control system based on user permissions. Additionally, it supports movie streaming using **HLS (HTTP Live Streaming)**.

### Features

- Movie and user management.
- User login and registration with JWT authentication.
- Movie reviews with the ability to add ratings and comments.
- Video streaming in HLS format.
- Access control based on user permissions.
- Secure endpoints using JWT authentication.
- User interaction pages rendered using Thymeleaf.
- Application monitoring with Grafana and Prometheus running in Docker containers.
- **Redis** for caching to enhance system performance.
- **RabbitMQ** for messaging, aiding in high-scale processing.
- **minIO** for storing video files.

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

## Setup and Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/ismael221/OpenStreamify
   ```

2. Configure the database in the `application.properties` file:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/database_name
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

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

### Example of an Authenticated Request with JWT

All routes, except login and registration, require a JWT token for authentication. To access protected routes, pass the token in the request header:

```http
Authorization: Bearer <your-jwt-token>
```

### Swagger UI

To see all available endpoints and their descriptions, access Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

## Project Structure

- `src/main/java/com/ismael/movies`: Contains the Java classes, including controllers, services, models, and repositories.
- `src/main/resources/templates`: Contains HTML pages rendered by Thymeleaf.
- `src/main/resources/static`: Contains static files like CSS and JavaScript.
- `src/main/resources/application.properties`: Application configuration.

## Security

The application uses **JWT tokens** for authentication and authorization. After logging in, the user receives a token that must be included in the header of all subsequent requests to protected routes.

## Future Improvements

- Integration with messaging services for high-scale processing using **RabbitMQ**.
- Implementation of a **CDN** to enhance streaming performance at scale.
- Support for multiple video qualities in HLS.
- Implementation of **Redis** cache to optimize system performance.

## Contributing

Contributions are welcome! Feel free to open an **issue** or submit a **pull request**.

## License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for more details.

---