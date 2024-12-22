package org.satyam.todolist;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.satyam.todolist.models.Todo;

import java.time.LocalDateTime;

import static io.dropwizard.jackson.Jackson.newObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    private static final ObjectMapper MAPPER = newObjectMapper();

    private final Todo todoForTest = new Todo(0, "Hello", "Hi",LocalDateTime.of(2024, 12, 19, 14, 0), LocalDateTime.of(2024, 12, 20, 14, 0), Todo.TaskStatus.WIP);

    //test to check the serialization and deserialization functioning
    @Test
    public void serializesToJson() throws Exception {
        final String expected =  MAPPER.writeValueAsString(
                MAPPER.readValue(getClass().getClassLoader().getResource("fixtures/todo.json"), Todo.class));

        assertThat(MAPPER.writeValueAsString(todoForTest)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJson() throws Exception {
        assertThat(MAPPER.readValue(getClass().getClassLoader().getResource("fixtures/todo.json"), Todo.class)).isEqualTo(todoForTest);
    }
}
