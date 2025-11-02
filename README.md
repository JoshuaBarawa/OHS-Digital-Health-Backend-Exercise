OHS Digital Health Backend Exercise - Submission

Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL installed and running locally (Update your credentials in
  src/main/resources/application.properties)


Getting Started
1. Clone the project from github: https://github.com/JoshuaBarawa/OHS-Digital-Health-Backend-Exercise.git
2. Navigate to ohs-backend-developer-exercise
3. Run: mvn clean spring-boot:run
4. Swagger API docs runs at http://localhost:8080/swagger-ui/index.html


Testing
- Run "mvn test" to execute all tests.

Notes

- Gender values: MALE or FEMALE
- Encounter classes: INPATIENT, OUTPATIENT, EMERGENCY
- validation errors return clear messages with proper HTTP status codes