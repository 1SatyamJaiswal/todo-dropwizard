package org.satyam.todolist.db;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.satyam.todolist.models.Todo;
import java.time.LocalDateTime;
import java.util.List;

public interface TodoDAO {
    @SqlQuery("SELECT * FROM todolist")
    List<Todo> getTodos();

    @SqlQuery("SELECT * FROM todolist WHERE id = :id")
    Todo getTodoById(@Bind("id") int id);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO todolist (id, title, description, startDateTime, targetDateTime, status) values (null, :title, :description, :startDateTime, :targetDateTime, :status)")
    int createTodo(@Bind("title") String title, @Bind("description") String description, @Bind("startDateTime")LocalDateTime startDateTime, @Bind("targetDateTime") LocalDateTime targetDateTime, @Bind("status")Todo.TaskStatus status);

    @SqlUpdate("UPDATE todolist SET title = :title, description = :description, startDateTime = :startDateTime, targetDateTime = :targetDateTime, status = :status WHERE id = :id")
    void updateTodo(@Bind("id") int id, @Bind("title") String title, @Bind("description") String description, @Bind("startDateTime")LocalDateTime startDateTime, @Bind("targetDateTime") LocalDateTime targetDateTime, @Bind("status")Todo.TaskStatus status);

    @SqlUpdate("DELETE FROM todolist WHERE id = :id")
    void deleteTodo(@Bind("id") int id);
}
