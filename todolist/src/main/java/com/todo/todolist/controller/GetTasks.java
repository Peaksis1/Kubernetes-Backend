package com.todo.todolist.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/tasks")
public class GetTasks {

    @GetMapping("")
    public List<String> getAllTasks() {
        return Arrays.asList("Task 1", "Task 2", "Task 3 test 3.2", "Task 4.2");
    }

}
