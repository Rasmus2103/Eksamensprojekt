package com.example.eksamensprojekt.controllers;
import com.example.eksamensprojekt.repository.IProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class ProjectController {
    private IProjectRepository projectRepository;

    public ProjectController(ApplicationContext context, @Value("eksamensprojekt_DB") String impl) {
        this.projectRepository =(IProjectRepository) context.getBean(impl);
    }
}
