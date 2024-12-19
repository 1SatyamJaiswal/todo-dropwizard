package org.satyam.todolist.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.Jdbi;
import org.satyam.todolist.db.TodoDAO;
import org.satyam.todolist.models.Todo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Path("/todo")
@Produces(MediaType.APPLICATION_JSON)
public class TodoResource {
    private final TodoDAO todoDAO;

    public TodoResource(Jdbi jdbi){
        todoDAO = jdbi.onDemand(TodoDAO.class);
    }

    @GET
    public Response getAll(){
        List<Todo> todos= todoDAO.getTodos();
        return Response.ok(todos).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id){
        Todo todo = todoDAO.getTodoById(id);
        return Response.ok(todo).build();
    }

    @POST
    public Response createTodo(Todo todo) throws URISyntaxException {
        int id = todoDAO.createTodo(todo.getTitle(), todo.getDescription(), todo.getStartDateTime(), todo.getTargetDateTime(), todo.getStatus());
        return Response.created(new URI(String.valueOf(id))).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTodo(@PathParam("id") int id, Todo todo) {
        todoDAO.updateTodo(id, todo.getTitle(), todo.getDescription(), todo.getStartDateTime(), todo.getTargetDateTime(), todo.getStatus());
        return Response.ok(new Todo(id, todo.getTitle(), todo.getDescription(), todo.getStartDateTime(), todo.getTargetDateTime(), todo.getStatus())).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTodo(@PathParam("id") int id){
        todoDAO.deleteTodo(id);
        return Response.noContent().build();
    }
}