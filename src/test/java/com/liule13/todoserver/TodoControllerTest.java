package com.liule13.todoserver;

import com.liule13.todoserver.entity.Todo;
import com.liule13.todoserver.repository.TodoRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void cleanUp() {
        todoRepository.deleteAll();
    }

    @Test
    void should_return_empty_list_when_index_with_no_any_todo() throws Exception {
        MockHttpServletRequestBuilder request = get("/todos").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void should_return_a_one_todo_when_index_with_one_todo() throws Exception {
        Todo todo = new Todo(null, "first todo", false);
        todoRepository.save(todo);
        MockHttpServletRequestBuilder request = get("/todos").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].text").value("first todo"))
                .andExpect(jsonPath("$[0].done").value(false));
    }

    @Test
    void should_return_a_todo_when_create_new_todo() throws Exception {
        String requestBody = """
                        {
                            "text": "first todo",
                            "done": false
                        }
                """;
        MockHttpServletRequestBuilder request = post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        mockMvc.perform(request).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.text").value("first todo"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void should_return_422_when_create_with_empty_text() throws Exception {
        String requestBody = """
                        {
                            "text": "",
                            "done": false
                        }
                """;
        MockHttpServletRequestBuilder request = post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        mockMvc.perform(request).andExpect(status().isUnprocessableEntity());
    }

    //Scenario: Reject missing required field "text"
    @Test
    void should_return_422_when_create_with_missing_text() throws Exception {
        String requestBody = """
                        {
                            "done": false
                        }
                """;
        MockHttpServletRequestBuilder request = post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        mockMvc.perform(request).andExpect(status().isUnprocessableEntity());
    }

    //Scenario: Ignore client-sent id
    @Test
    void should_ignore_client_sent_id_when_create_new_todo() throws Exception {
        String requestBody = """
                        {
                            "id": "client-sent-id",
                            "text": "first todo",
                            "done": false
                        }
                """;
        MockHttpServletRequestBuilder request = post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        mockMvc.perform(request).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(Matchers.not("client-sent-id")))
                .andExpect(jsonPath("$.text").value("first todo"))
                .andExpect(jsonPath("$.done").value(false));
    }

    //Scenario: Update both fields
    @Test
    void should_return_updated_todo_when_put_todo_with_existed_id() throws Exception {
        Todo todo = new Todo("123", "existing todo", false);
        todoRepository.save(todo);
        String requestBody = """
                                {
                                    "id": "123",
                                    "text": "updated todo",
                                    "done": true
                                }
                """;
        MockHttpServletRequestBuilder request = put("/todos/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.text").value("updated todo"))
                .andExpect(jsonPath("$.done").value(true));
    }
}
