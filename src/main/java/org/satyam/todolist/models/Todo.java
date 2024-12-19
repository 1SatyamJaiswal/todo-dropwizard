package org.satyam.todolist.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.time.LocalDateTime;

public class Todo {
    private int id;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime targetDateTime;
    private TaskStatus status;

    public enum TaskStatus {
        Todo,
        WIP,
        Done
    };

    public Todo(int id, String title, String description, LocalDateTime startDateTime, LocalDateTime targetDateTime, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.targetDateTime = targetDateTime;
        this.status = status;
    }

    public Todo() {
        // Default constructor required for JDBI
    }

    @JsonProperty
    @ColumnName("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty
    @ColumnName("title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty
    @ColumnName("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty
    @ColumnName("startDateTime")
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    @JsonProperty
    @ColumnName("targetDateTime")
    public LocalDateTime getTargetDateTime() {
        return targetDateTime;
    }

    public void setTargetDateTime(LocalDateTime targetDateTime) {
        this.targetDateTime = targetDateTime;
    }

    @JsonProperty
    @ColumnName("status")
    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    //To handle string received as the task status
    public static TaskStatus fromString(String status) {
        return switch (status.toLowerCase()) {
            case "todo" -> TaskStatus.Todo;
            case "wip" -> TaskStatus.WIP;
            case "done" -> TaskStatus.Done;
            default -> throw new IllegalArgumentException("Unknown status: " + status);
        };
    }
}
