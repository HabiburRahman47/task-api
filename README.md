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
- Get a task by Id
- Update a task 
- Request validation
- PostgreSQL persistence
- Versioned database migrations
- Pagination and Filtering

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
The endpoint supports pagination and sorting.
Query parameters:
| Parameter  | Default | Description   |
|------------|----------|---------------|
| `page`     | `0`      | Zero-based page number |
| `size`     | `10`     | Number of task per page|
| `sort`    | `createdAt,desc`| Sort property and direction|

Example:
```bash
curl -i "http://localhost:8084/api/tasks"
```
```bash
curl -i "http://localhost:8084/api/tasks?page-0&size=10"
```
```bash
curl -i "http://localhost:8084/api/tasks?page-0&size=10&sort=title,asc"
```

Response:

```json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 0,
  "totalPages": 0,
  "first": true,
  "last": true
}
```

Allowed sort properties:

- `id`
- `title`
- `status`
- `priority`
- `createdAt`

Response status:

```text
200 OK
```
### Get a task by ID
```http
GET /api/tasks/{id}
```
Example:
```bash
curl -i http://localhost:8084/api/tasks/1
```

Response:
```json
{
  "id": 1,
  "title": "Learn Spring Boot",
  "description": "Build a REST API",
  "status": "TODO",
  "priority": "MEDIUM",
  "createdAt": "2026-09-18T00:00:00Z"
} 
```
Response status:
- `200 OK` when the task exists.
- `404 Not Found` when the task does not exist.

### Update a task
```http
PATCH /api/tasks/{id}
```
The request may contain one or more fields:

```json
{
  "status": "IN_PPROGRESS",
  "priority": "HIGH"
}
```

Example:
```bash
curl -i -X PATCH http://localhost:8084/api/tasks/1 \
-H "Content-Type: application/json" \
-d '{
  "status": "IN_PROGRESS",
  "priority": "HIGH"
}'
```
### Filtering tasks
Tasks can be filtered by statu and priority
Filter by status:
```bash
curl -i "http://localhost:8084/api/tasks?status=TODO"
```
Filter by priority:

```bash
curl -i "http://localhost:8084/api/tasks?priority=HIGH"
```

Combine filters:

```bash
curl -i "http://localhost:8084/api/tasks?status=TODO&priority=HIGH"
```

### Search tasks
Search is case-insensitive and checks both the task title and description.

Search by title and description
```bash
curl -i "http://localhost:8084/api/tasks?search=spring"
```

Search with status and priority
```bash
curl -i "http://localhost:8084/api/tasks?search=spring&status=TODP&priority=LOW"
```
Search with pagination and sorting
```bash
curl -i "http://localhost:8084/api/tasks?search=spring&page=0&size=10&sort=createdAt,desc"
```


Supported status values:
-  `TODO`
- `IN_PROGRESS`
- `DONE`

Supported priority values:
- `LOW`
- `MEDIUM`
- `HIGH`
Response statuses:
- `200 OK` when the task is updated.
- `400 Bad Request` when the request is invalid.
- `404 Not Found` when the task does not exist.


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
