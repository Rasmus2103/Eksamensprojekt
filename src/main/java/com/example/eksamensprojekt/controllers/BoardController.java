package com.example.eksamensprojekt.controllers;
import com.example.eksamensprojekt.model.Board;
import com.example.eksamensprojekt.repository.IBoardRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class BoardController extends PMController {
    //private IBoardRepository boardRepository;

    public BoardController(ApplicationContext context, @Value("board_DB") String impl) {
        //this.boardRepository =(IBoardRepository) context.getBean(impl);
    }

}
