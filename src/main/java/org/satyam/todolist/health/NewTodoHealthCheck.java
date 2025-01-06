package org.satyam.todolist.health;

import com.codahale.metrics.health.HealthCheck;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.satyam.todolist.models.Todo;
import org.satyam.todolist.models.Todo.TaskStatus;
import java.time.LocalDateTime;

public class NewTodoHealthCheck extends HealthCheck {
    private final JerseyClient client;

    public NewTodoHealthCheck() {
        this.client = JerseyClientBuilder.createClient();
    }

    @Override
    protected Result check() throws Exception {
        Todo testTodo = new Todo(
                0,
                "Health Check Todo",
                "Health Check Description",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                TaskStatus.Todo
        );

        Response response = client.target("http://localhost:8080/todo")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(testTodo, MediaType.APPLICATION_JSON));

        return response.getStatus() == 201
                ? Result.healthy()
                : Result.unhealthy("New Todo cannot be created!");
    }
}