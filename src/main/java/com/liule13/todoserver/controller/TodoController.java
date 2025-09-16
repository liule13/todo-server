package com.liule13.todoserver.controller;

import com.liule13.todoserver.entity.Todo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    @GetMapping
    public List<Todo> index() {
        return List.of();
    }
}
