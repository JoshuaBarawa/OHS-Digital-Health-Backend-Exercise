OHS Digital Health Backend Exercise - Submission

Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL installed and running locally (Update your credentials in
  src/main/resources/application.properties)


Getting Started
1. Clone the project from github: https://github.com/JoshuaBarawa/OHS-Digital-Health-Backend-Exercise.git
2. Navigate to the project directory: cd ohs-backend-developer-exercise
3. Run the application: mvn clean spring-boot:run
4. Access Swagger API docs at: http://localhost:8080/swagger-ui/index.html


How to Run the Project using Docker
1. Make sure you are in the project root directory where the Dockerfile is located, then run: docker build -t ohs-backend-developer-exercise .
2. Make sure you already have PostgreSQL running locally: docker run --name postgres-db -e POSTGRES_USER=your_username \
   -e POSTGRES_PASSWORD=your_password \
   -e POSTGRES_DB=digital_health \
   -p 5432:5432 -d postgres

3. Run the Spring Boot application container: docker run -p 8080:8080 \
   -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/digital_health \
   -e SPRING_DATASOURCE_USERNAME=your_username \
   -e SPRING_DATASOURCE_PASSWORD=your_password \
   ohs-backend-developer-exercise
4. Now your app is live at http://localhost:8080/swagger-ui/index.html

Testing
- Run "mvn test" to execute all tests.

Notes

- Gender values: MALE or FEMALE
- Encounter classes: INPATIENT, OUTPATIENT, EMERGENCY
- validation errors return clear messages with proper HTTP status codes