# Todo List Application

A RESTful Todo List application built with Dropwizard and MySQL, containerized with Docker.

## Prerequisites

- Java 21
- Maven
- Docker
- MySQL Server running locally
- MySQL database and user created with following credentials:
  ```sql
  CREATE DATABASE todolist;
  CREATE USER 'todolistuser'@'%' IDENTIFIED BY 'todolistpassword';
  GRANT ALL PRIVILEGES ON todolist.* TO 'todolistuser'@'%';
  FLUSH PRIVILEGES;
  ```

## Building and Running

### Local Development

1. Build the application:
```bash
mvn clean package
```

2. Run the application:
```bash
java -jar target/todolist-1.0-SNAPSHOT.jar server config.yml
```

### Docker Deployment

1. Build Docker image:
```bash
docker build -t todolist-app .
```

2. Run Docker container:
```bash
docker run -p 8080:8080 -p 8081:8081 todolist-app
```

For Linux systems, use:
```bash
docker run -p 8080:8080 -p 8081:8081 --add-host=host.docker.internal:host-gateway todolist-app
```

### Docker-Compose Deployment

1. To build and run the project
```bash
docker-compose up --build
```

2. To stop the project
```bash
docker-compose down
```

## API Endpoints

### Todo Tasks

#### GET /todo
- Retrieves all todo tasks
- Response: 200 OK
```json
[
  {
    "id": 1,
    "title": "Hi my name is Satyam",
    "description": "Hello",
    "startDateTime": "14:00 19-12-2024",
    "targetDateTime": "14:00 20-12-2024",
    "status": "Done"
  }
]
```

#### GET /todo/{id}
- Retrieves a specific todo task by ID
- Response: 200 OK
```json
{
  "id": 1,
  "title": "Task Title",
  "description": "Task Description",
  "startDateTime": "14:00 19-12-2024",
  "targetDateTime": "14:00 20-12-2024",
  "status": "WIP"
}
```

#### POST /todo
- Creates a new todo task
- Request Body:
```json
{
  "title": "New Task",
  "description": "Task Description",
  "startDateTime": "14:00 19-12-2024",
  "targetDateTime": "14:00 20-12-2024",
  "status": "WIP"
}
```
- Response: 201 Created

#### PUT /todo/{id}
- Updates an existing todo task
- Request Body:
```json
{
  "title": "Updated Task",
  "description": "Updated Description",
  "startDateTime": "14:00 19-12-2024",
  "targetDateTime": "14:00 20-12-2024",
  "status": "Done"
}
```
- Response: 200 OK

#### DELETE /todo/{id}
- Deletes a todo task
- Response: 204 No Content

## Task Status Values

Tasks can have the following status values:
- `WIP` (Work In Progress)
- `Done` (Completed)
- `Todo` (Not Started)

## Configuration

The application uses `config.yml` for configuration. Key configurations include:

```yaml
database:
  user: todolistuser
  password: todolistpassword
  url: jdbc:mysql://localhost/todolist
```

## Testing

Run the test suite with:
```bash
mvn test
```