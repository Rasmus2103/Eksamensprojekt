package com.example.eksamensprojekt.controllers;
import com.example.eksamensprojekt.repository.ITaskRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class TaskController {
    private ITaskRepository taskRepository;

    public TaskController(ApplicationContext context, @Value("eksamensprojekt_DB") String impl) {
        this.taskRepository =(ITaskRepository) context.getBean(impl);
    }
}