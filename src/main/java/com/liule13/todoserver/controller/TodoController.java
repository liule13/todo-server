package com.liule13.todoserver.controller;

import com.liule13.todoserver.entity.Todo;
import com.liule13.todoserver.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> index() {
        return todoService.index();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Todo addNewTodo(@RequestBody Todo todo) {
        if (todo.getText().isEmpty()) {
            throw new IllegalArgumentException("Todo text cannot be empty");
        }
        return todoService.addNewTodo(todo);
    }
}
