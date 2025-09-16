package com.liule13.todoserver.service;

import com.liule13.todoserver.entity.Todo;
import com.liule13.todoserver.exception.TodoNotFoundException;
import com.liule13.todoserver.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> index() {
        return todoRepository.findAll();
    }

    public Todo addNewTodo(Todo todo) {
        todo.setId(UUID.randomUUID().toString());
        return todoRepository.save(todo);
    }

    public Todo updateTodo(String id, Todo todo) {
        Optional<Todo> updateTodo = todoRepository.findById(id);
        if (updateTodo.isEmpty()) {
            throw new TodoNotFoundException("Todo with id " + id + " not found");
        }
        Todo existingTodo = updateTodo.get();
        existingTodo.setText(todo.getText());
        existingTodo.setDone(todo.getDone());
        return todoRepository.save(existingTodo);
    }
}
