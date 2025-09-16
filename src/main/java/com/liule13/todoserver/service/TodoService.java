package com.liule13.todoserver.service;

import com.liule13.todoserver.entity.Todo;
import com.liule13.todoserver.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> index() {
        return todoRepository.findAll();
    }
}
