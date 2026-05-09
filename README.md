# **Job Applications Tracker**

A full-stack backend application built with Spring Boot for managing and tracking job applications. Users can create, update, search, and manage applications through a RESTful API.

## Features

- Create job applications
- Update application details
- Search applications 
- Delete applications
- RESTful API architecture
- DTO validation
- Integration & unit testing
- Docker support
- PostgreSQL database integration

## Tech Stack

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* Maven
* PostgreSQL
* Testing
* Mockito
* Spring Boot Test
* Docker
* Docker Compose

## Project Structure

```
job-applications-tracker/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │
│   └── test/
│
├── target/
├── Dockerfile
├── compose.yaml
├── pom.xml
├── .dockerignore
├── .gitignore
└── README.md
```


## API Endpoints

| Method | Endpoint                                  | Description                                |
|--------|-------------------------------------------|--------------------------------------------|
| POST   | `/job-applications`                       | Create application                         |
| GET    | `/job-applications`                       | Get all applications                       |
| GET    | `/job-applications/{id}`                  | Get application by ID                      |
| GET    | `/job-applications/company/{companyName}` | Get applications by company name           |
| GET    | `/job-applications/status/{status}`       | Get applications by status                 |
| GET    | `/job-applications/job-type/{jobType}`    | Get applications by job type               |
| GET    | `/job-applications/job-title/{jobTitle}`  | Get applications by job title              |
| GET    | `/job-applications/location/{location}`   | Get applications by location               |
| PATCH  | `/job-applications/{id}/status"`          | Update application status                  |
| DELETE | `/job-applications/{id}"`                 | Delete an application using application id |


## Running the Application Locally

### 1. **Clone the Repository**

   git clone <repository-url>\
   cd job-applications-tracker

### 2. Configure PostgreSQL

   **Update your application.properties:**
   
   spring.datasource.url=jdbc:postgresql://localhost:5432/job_tracker
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   
   spring.jpa.hibernate.ddl-auto=update

### 3. Run the Application

##### Using Maven:
./mvnw spring-boot:run

Or:

mvn spring-boot:run\

#### Running with Docker
- Build and Start Containers: docker compose up --build\
- Stop Containers: docker compose down\
- Running Tests: mvn test

## **Example JSON Request**

### Create Application
```
{
   "companyName":"Google",
   "jobTitle":"Backend Developer",
   "jobType":"INTERNSHIP", 
   "location": "Pretoria"
}
```

## **Future Improvements**
* Authentication & Authorization
* Pagination & Sorting
* Swagger/OpenAPI documentation

## **Learning Goals**
**This project was built to strengthen understanding of:**

* Spring Boot architecture
* REST API development
* Database integration with JPA
* Docker containerization
* Testing in Spring Boot
* Backend project structure and best practices