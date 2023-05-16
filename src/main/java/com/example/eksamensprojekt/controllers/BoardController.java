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

@Controller
public class BoardController extends PMController {
    private IBoardRepository boardRepository;

    public BoardController(ApplicationContext context, @Value("board_DB") String impl) {
        this.boardRepository =(IBoardRepository) context.getBean(impl);
    }

    @GetMapping("board/{boardid}")
    public String getBoard(@PathVariable("boardid") int boardid, Model model, HttpSession session) {
        Board board = boardRepository.getSpecificBoard(boardid);
        model.addAttribute("board", board);

        return isLogged(session) ? "board" : "index";
    }
}
