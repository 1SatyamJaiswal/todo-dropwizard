package org.satyam.todolist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.DropwizardTestSupport;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.satyam.todolist.models.Todo;

import java.time.LocalDateTime;
import java.util.List;

import static io.dropwizard.jackson.Jackson.newObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppResourceTest {

    // Test server setup
    public static final DropwizardTestSupport<TodoListConfiguration> SUPPORT =
            new DropwizardTestSupport<>(App.class,
                    ResourceHelpers.resourceFilePath("config.yml"));

    private static Client client;
    private static String baseUrl;

    // Reuse the ObjectMapper that worked for you in AppTest
    private static final ObjectMapper MAPPER = newObjectMapper().registerModule(new JavaTimeModule());

    @BeforeAll
    public static void setUp() throws Exception {
        SUPPORT.before();
        baseUrl = "http://localhost:" + SUPPORT.getLocalPort();

        // Create a JacksonJsonProvider with our configured ObjectMapper
        JacksonJsonProvider jsonProvider = new JacksonJsonProvider(MAPPER);

        // Create the client with the custom provider
        client = JerseyClientBuilder.createClient()
                .register(jsonProvider);
    }

    @AfterAll
    public static void tearDown() {
        SUPPORT.after();
    }

    @Test
    @Order(1)
    public void testCreateTodo() {
        Todo todo = new Todo(0, "New Todo", "Description",
                LocalDateTime.of(2024, 12, 19, 14, 0),
                LocalDateTime.of(2024, 12, 20, 14, 0),
                Todo.TaskStatus.WIP);

        Response response = client.target(baseUrl + "/todo")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(todo, MediaType.APPLICATION_JSON));

        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        String locationHeader = response.getHeaderString("Location");
        assertThat(locationHeader).isNotBlank();
    }

    @Test
    @Order(2)
    public void testGetAllTodos() {
        Response response = client.target(baseUrl + "/todo")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        List<Todo> todos = response.readEntity(new GenericType<List<Todo>>() {});
        assertThat(todos).isNotEmpty();
    }

    @Test
    @Order(3)
    public void testGetTodoById() {
        Todo todo = new Todo(0, "Fetch Todo", "Fetch Description",
                LocalDateTime.of(2024, 12, 19, 14, 0),
                LocalDateTime.of(2024, 12, 20, 14, 0),
                Todo.TaskStatus.WIP);

        Response createResponse = client.target(baseUrl + "/todo")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(todo));

        String locationHeader = createResponse.getHeaderString("Location");
        int id = Integer.parseInt(locationHeader.substring(locationHeader.lastIndexOf('/') + 1));

        Response response = client.target(baseUrl + "/todo/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Todo resultTodo = response.readEntity(Todo.class);
        assertThat(resultTodo.getId()).isEqualTo(id);
        assertThat(resultTodo.getTitle()).isEqualTo(todo.getTitle());
    }

    @Test
    @Order(4)
    public void testUpdateTodo() {
        Todo todo = new Todo(0, "Update Todo", "Update Description", LocalDateTime.of(2024, 12, 19, 14, 0), LocalDateTime.of(2024, 12, 20, 14, 0), Todo.TaskStatus.WIP);

        Response createResponse = client.target(baseUrl + "/todo")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(todo));

        String locationHeader = createResponse.getHeaderString("Location");
        int id = Integer.parseInt(locationHeader.substring(locationHeader.lastIndexOf('/') + 1));

        Todo updatedTodo = new Todo(id ,"Updated Title", "Updated Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), Todo.TaskStatus.Done);

        Response response = client.target(baseUrl + "/todo/" + id)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(updatedTodo));

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Todo resultTodo = response.readEntity(Todo.class);
        assertThat(resultTodo.getTitle()).isEqualTo("Updated Title");
        assertThat(resultTodo.getStatus()).isEqualTo(Todo.TaskStatus.Done);
    }

    @Test
    @Order(5)
    public void testDeleteTodo() {
        Todo todo = new Todo(0, "Delete Todo", "Delete Description", LocalDateTime.of(2024, 12, 19, 14, 0), LocalDateTime.of(2024, 12, 20, 14, 0), Todo.TaskStatus.WIP);

        Response createResponse = client.target(baseUrl + "/todo")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(todo));

        String locationHeader = createResponse.getHeaderString("Location");
        int id = Integer.parseInt(locationHeader.substring(locationHeader.lastIndexOf('/') + 1));

        Response response = client.target(baseUrl + "/todo/" + id)
                .request()
                .delete();

        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());

        // Verify Deletion
        Response getResponse = client.target(baseUrl + "/todo/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(getResponse.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }
}
