# Task API

A REST API for task management system with Spring Boot and PostgreSQL.

## Technology Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Flyway
- Maven
- JUnit
- Mockito

## Feature

- Create a task
- List all tasks
- Request validation
- PostgreSQL persistence
- Versioned database migrations

## Prerequisites

Install the following:
- Java 21 or newer
- Maven
- PostgreSQL
- Git

Verity Java:
```bash
java -version
```

Verity Maven
```bash
mvn -version
```

## Database Setup

Create the PostgreSQL user and databse:
```sql
create user task_user with password 'task_password';
create database task_db owner task_user;
grant all privileges on database task_db to task_user;
```

## Configuration
The application uses these database settings:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/task_db
spring.datasource.username=task_user
spring.datasource.password=task_password
```

for real deployment, store credentials in environment vairables instead of committing them to source control.

## Run the Application

Start the application with Maven:

```bash
./mvnw spring-boot:run
```

The application runs on:

```text
http://localhost:8084
```

## Build the Application

```bash
./mvnw clean package
```

Run the packaged JAR:

```bash
java -jar target/*.jar
```

## Run Tests

```bash
./mvnw test
```

## API Endpoints

### Create a task

```http
POST /api/tasks
```

Request:

```json
{
  "title": "Learn Spring Boot",
  "description": "Build a REST API"
}
```

Response:

```json
{
  "id": 1,
  "title": "Learn Spring Boot",
  "description": "Build a REST API",
  "status": "TODO",
  "priority": "MEDIUM",
  "createdAt": "2026-09-17T00:00:00Z"
}
```

Response status:

```text
201 Created
```

### List tasks

```http
GET /api/tasks
```

Response:

```json
[
  {
    "id": 1,
    "title": "Learn Spring Boot",
    "description": "Build a REST API",
    "status": "TODO",
    "priority": "MEDIUM",
    "createdAt": "2026-09-17T00:00:00Z"
  }
]
```

Response status:

```text
200 OK
```
## Database Migrations

Flyway migration files are located at:

```text
src/main/resources/db/migration
```

Current migrations:

```text
V1__create_tasks_table.sql
V2__add_priority_to_tasks.sql
```

Applied migrations are recorded in:

```text
flyway_schema_history
```

Never modify a migration that has already been applied. Create a new migration instead.

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── task_api/
│   │       └── task/
│   │           ├── Task.java
│   │           ├── TaskController.java
│   │           ├── TaskRepository.java
│   │           ├── TaskResponse.java
│   │           ├── TaskService.java
│   │           └── TaskStatus.java
│   └── resources/
│       ├── db/
│       │   └── migration/
│       └── application.properties
└── test/
    └── java/
```

## Development Workflow

1. Create or update the Java code.
2. Add or update tests.
3. Add a database migration if the schema changes.
4. Run the test suite.
5. Test the API manually.
6. Update this README.
7. Commit the changes to Git.

## Document checklist
When adding a feature:
- Describe the feature in the README.
- Document new endpoints.
- Document expected HTTP status codes.
- Add validation rules.
- Add database migration details.
- Add automated tests.
- Update the OpenAPI annotations.
