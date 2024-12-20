package org.satyam.todolist.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @JsonProperty
    @ColumnName("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotEmpty
    @JsonProperty
    @ColumnName("title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotEmpty
    @JsonProperty
    @ColumnName("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    @JsonProperty
    @ColumnName("startDateTime")
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    @JsonProperty
    @ColumnName("targetDateTime")
    public LocalDateTime getTargetDateTime() {
        return targetDateTime;
    }

    public void setTargetDateTime(LocalDateTime targetDateTime) {
        this.targetDateTime = targetDateTime;
    }

    @NotNull
    @JsonProperty
    @ColumnName("status")
    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    //Validation to check that the target date stays always after start date
    @AssertTrue(message = "Target date must be after start date")
    private boolean isTargetDateValid() {
        if (startDateTime == null || targetDateTime == null) {
            return true;
        }
        return targetDateTime.isAfter(startDateTime);
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
