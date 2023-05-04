package com.example.eksamensprojekt.controllers;

import com.example.eksamensprojekt.model.*;
import com.example.eksamensprojekt.repository.RepositoryDB;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class PMController {
    private RepositoryDB repositoryDB;

    public PMController(RepositoryDB repositoryDB) {
        this.repositoryDB = repositoryDB;
    }

    private boolean isLogged(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null;
    }

    @GetMapping("login")
    public String login() {
        return "index";
    }

    @PostMapping("login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, Model model) {
        User user = repositoryDB.getUser(repositoryDB.getUserid(username));
        if(user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            return "redirect:/userProjects/" + user.getUserid();
        }
        model.addAttribute("wrongCredentials", true);
        return "index";
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        // invalidate session and return landing page
        session.invalidate();
        return "index";
    }

    @GetMapping("userProjects/{id}")
    public String getUserProjects(@PathVariable("id") int userid, Model model, HttpSession session) {
        if(isLogged(session)) {
            User user = (User) session.getAttribute("user");
            if (user.getUserid() == userid) {
                model.addAttribute("user", user);

                List<Project> projects = repositoryDB.getProjects(userid);
                model.addAttribute("projects", projects);
                return "userProjects";
            } else {
                return "redirect:/index";
            }
        } else {
            return "index";
        }
    }

    @GetMapping("project/{projectid}")
    public String getProject(@PathVariable("projectid") int projectid, Model model, HttpSession session) {
        Project project = repositoryDB.getSpecificProject(projectid);
        model.addAttribute("project", project);

        int boardid = 0;
        List<Story> stories = repositoryDB.getStories(boardid);
        model.addAttribute("stories", stories);

        List<Board> boards = repositoryDB.getBoards(projectid);
        model.addAttribute("boards", boards);

        return isLogged(session) ? "project" : "index";
    }

    @GetMapping("storylist/{boardid}")
    public String getStories(@PathVariable("boardid") int boardid, Model model, HttpSession session) {
        List<Story> stories = repositoryDB.getStories(boardid);
        model.addAttribute("stories", stories);

        return isLogged(session) ? "storylist" : "index";
    }


    @GetMapping("story/{storyid}")
    public String getStory(@PathVariable("storyid") int storyid, Model model, HttpSession session) {
        Story story = repositoryDB.getSpecificStory(storyid);
        model.addAttribute("story", story);

        List<Task> tasks = repositoryDB.getTasks(storyid);
        model.addAttribute("tasks", tasks);

        return isLogged(session) ? "story" : "index";
    }

    @GetMapping("board/{boardid}")
    public String getBoard(@PathVariable("boardid") int boardid, Model model, HttpSession session) {
        Board board = repositoryDB.getSpecificBoard(boardid);
        model.addAttribute("board", board);

        return isLogged(session) ? "board" : "index";
    }

    @GetMapping("task/{taskid}")
    public String getTask(@PathVariable("taskid") int taskid, Model model, HttpSession session) {
        Task task = repositoryDB.getSpecificTask(taskid);
        model.addAttribute("task", task);

        return isLogged(session) ? "task" : "index";
    }

}
