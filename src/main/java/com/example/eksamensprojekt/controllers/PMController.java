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

    public PMController(ApplicationContext context, @Value("user_DB") String implUser, @Value("project_DB") String implProject, @Value("board_DB") String implBoard, @Value("story_DB") String implStory, @Value("task_DB") String implTask) {
        this.userRepository = (IUserRepository) context.getBean(implUser);
        this.projectRepository = (IProjectRepository) context.getBean(implProject);
        this.boardRepository = (IBoardRepository) context.getBean(implBoard);
        this.storyRepository = (IStoryRepository) context.getBean(implStory);
        this.taskRepository = (ITaskRepository) context.getBean(implTask);
    }

    public PMController() {
    }

    protected boolean isLogged(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null;
    }

}
