package com.example.eksamensprojekt.controllers;
import com.example.eksamensprojekt.repository.IBoardRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class BoardController {
    private IBoardRepository boardRepository;

    public BoardController(ApplicationContext context, @Value("eksamensprojekt_DB") String impl) {
        this.boardRepository =(IBoardRepository) context.getBean(impl);
    }
}
