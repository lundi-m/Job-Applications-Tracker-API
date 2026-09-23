# Job Applications Tracker

A backend REST API built with Spring Boot for managing and tracking job applications. The application supports user authentication, JWT-based security, application management, filtering, validation, and PostgreSQL persistence.

## Features

* User registration and authentication
* JWT-based authentication and authorization
* Access and refresh token support
* Refresh token invalidation
* Create job applications
* Get all applications for the authenticated user
* Get a job application by ID
* Update application details
* Update application status
* Delete applications
* Filter applications by multiple criteria
* DTO-based request and response handling
* Request validation
* Exception handling
* PostgreSQL database integration
* Unit, controller, repository, and integration testing
* Docker and Docker Compose support

## Tech Stack

* Java 17
* Spring Boot
* Spring Web
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* Lombok
* JUnit
* Mockito
* MockMvc
* Docker
* Docker Compose

## Architecture

The application follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

DTOs are used to separate API request/response models from persistence entities, while Spring Security handles authentication and authorization.

## Project Structure

```text
job-applications-tracker/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/lundim/job_applications_tracker/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── exception/
│   │   │       ├── repository/
│   │   │       ├── security/
│   │   │       └── service/
│   │   │
│   │   └── resources/
│   │
│   └── test/
│
├── Dockerfile
├── compose.yaml
├── pom.xml
├── .dockerignore
├── .gitignore
└── README.md
```

## API Endpoints

### Authentication

| Method | Endpoint                          | Description                  |
| ------ | --------------------------------- | ---------------------------- |
| POST   | `/job-applications/auth/register` | Register a new user          |
| POST   | `/job-applications/auth/login`    | Authenticate a user          |
| POST   | `/job-applications/auth/refresh`  | Refresh an access token      |
| POST   | `/job-applications/auth/logout`   | Invalidate the refresh token |

### Job Applications

| Method | Endpoint                        | Description                               |
| ------ | ------------------------------- | ----------------------------------------- |
| POST   | `/job-applications`             | Create a job application                  |
| GET    | `/job-applications`             | Get the authenticated user's applications |
| GET    | `/job-applications/{id}`        | Get a job application by ID               |
| PATCH  | `/job-applications/{id}`        | Update application details                |
| PATCH  | `/job-applications/{id}/status` | Update application status                 |
| DELETE | `/job-applications/{id}`        | Delete a job application                  |

### Filtering

Applications can be filtered using query parameters such as:

* `companyName`
* `jobTitle`
* `status`
* `jobType`
* `location`

Example:

```text
GET /job-applications?companyName=Google&status=APPLIED
```

Multiple filters can be combined in a single request.

## Authentication

The API uses JWT-based authentication.

After successfully logging in, the client receives an access token and refresh token. Protected endpoints require a valid access token.

Refresh tokens can be invalidated during logout, preventing them from being reused.

## Example JSON Request

### Create Application

```json
{
  "companyName": "Google",
  "jobTitle": "Backend Developer",
  "jobType": "INTERNSHIP",
  "location": "Pretoria",
  "dateApplied": "2026-09-23"
}
```

## Running the Application Locally

### 1. Clone the Repository

```bash
git clone https://github.com/lundi-m/Job-Applications-Tracker-API
cd job-applications-tracker
```

### 2. Configure PostgreSQL

Create a PostgreSQL database and configure the application with your database credentials.

For local development, configure the datasource in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/job_tracker
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
```

### 3. Run the Application

Using the Maven wrapper:

```bash
./mvnw spring-boot:run
```

Or with Maven:

```bash
mvn spring-boot:run
```

### 4. Run Tests

```bash
mvn test
```

## Running with Docker

Build and start the application and PostgreSQL containers:

```bash
docker compose up --build
```

Stop the containers:

```bash
docker compose down
```

The application will be available at:

```text
http://localhost:8080
```

The PostgreSQL database runs inside the Docker Compose network and is accessed by the application using the `db` service name.

## Testing

The project includes:

* Unit tests
* Controller tests
* Repository tests
* Integration tests

Testing tools include:

* JUnit
* Mockito
* Spring Boot Test
* MockMvc
* H2 for test database scenarios

## Future Improvements

Potential future improvements include:
* Deployment

