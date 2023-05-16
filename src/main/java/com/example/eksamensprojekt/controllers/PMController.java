package com.example.eksamensprojekt.controllers;

import com.example.eksamensprojekt.dto.ProjectDTOForm;
import com.example.eksamensprojekt.model.*;
import com.example.eksamensprojekt.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class PMController {
    protected IUserRepository userRepository = new UserRepository();
    protected IProjectRepository projectRepository = new ProjectRepository();
    protected IBoardRepository boardRepository = new BoardRepository();
    protected IStoryRepository storyRepository = new StoryRepository();
    protected ITaskRepository taskRepository = new TaskRepository();

    public PMController(IUserRepository userRepository, IProjectRepository projectRepository, IBoardRepository boardRepository, IStoryRepository storyRepository, ITaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.boardRepository = boardRepository;
        this.storyRepository = storyRepository;
        this.taskRepository = taskRepository;
    }

    public PMController() {
    }

    protected boolean isLogged(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null;
    }

}
