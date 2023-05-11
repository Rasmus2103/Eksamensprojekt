package com.example.eksamensprojekt.controllers;
import com.example.eksamensprojekt.repository.IStoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class StoryController {
    private IStoryRepository storyRepository;

    public StoryController(ApplicationContext context, @Value("eksamensprojekt_DB") String impl) {
        this.storyRepository =(IStoryRepository) context.getBean(impl);
    }
}
