**OHS Digital Health Backend Exercise - Submission**

**Prerequisites**

Before running the project, ensure the following are installed on your system:

- Java 17+
- Maven 3.8+
- PostgreSQL (installed and running locally)
- Docker (for containerized setup)

**Environment Setup**

1. Local Database Configuration

2. Update your database credentials in: *src/main/resources/application.properties*




Example:

spring.datasource.url=jdbc:postgresql://localhost:5432/digital_health
spring.datasource.username=your_username
spring.datasource.password=your_password

**Running the Application Locally**

Clone the repository:
git clone https://github.com/JoshuaBarawa/OHS-Digital-Health-Backend-Exercise.git

Navigate to the project directory:
cd ohs-backend-developer-exercise

Start PostgreSQL (if not already running).
Run the Spring Boot app:
mvn clean spring-boot:run

Access Swagger documentation:
http://localhost:8080/swagger-ui/index.html

**Running the Application with Docker**

**Option 1: Build and Run Locally with Docker**

Ensure PostgreSQL is running locally, or start it with Docker:

docker run --name postgres-db -e POSTGRES_USER=your_username \
-e POSTGRES_PASSWORD=your_password \
-e POSTGRES_DB=digital_health \
-p 5432:5432 -d postgres


Build the Docker image:

docker build -t ohs-backend-developer-exercise .


Run the Spring Boot container:

docker run -p 8080:8080 \
-e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/digital_health \
-e SPRING_DATASOURCE_USERNAME=your_username \
-e SPRING_DATASOURCE_PASSWORD=your_password \
ohs-backend-developer-exercise


Access the app:

http://localhost:8080/swagger-ui/index.html

**Option 2: Pull and Run from Docker Hub**

If you prefer not to build the image yourself, pull the prebuilt one directly:

Pull the published image:

docker pull barawa/ohs-backend-developer-exercise:1.0.0


Run the container:

docker run -p 8080:8080 \
-e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/digital_health \
-e SPRING_DATASOURCE_USERNAME=your_username \
-e SPRING_DATASOURCE_PASSWORD=your_password \
barawa/ohs-backend-developer-exercise:1.0.0


Open Swagger UI:

http://localhost:8080/swagger-ui/index.html


**Running Tests**

Run all unit and integration tests using Maven:

mvn test

**Notes:**
1. Gender values: MALE or FEMALE
2. Encounter classes: INPATIENT, OUTPATIENT, or EMERGENCY
3. Validation errors return clear messages with proper HTTP status codes.
4. Swagger is automatically enabled at /swagger-ui/index.html.