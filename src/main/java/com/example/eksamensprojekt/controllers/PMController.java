package com.example.eksamensprojekt.controllers;

import com.example.eksamensprojekt.model.*;
import com.example.eksamensprojekt.repository.RepositoryDB;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("createproject/{id}")
    public String createProject(@PathVariable("id") int userid, Model model, HttpSession session) {
        Project project = new Project();
        model.addAttribute("project", project);

        User user = repositoryDB.getUser(userid);
        model.addAttribute("user", user);
        return isLogged(session) ? "createproject" : "index";
    }

    @PostMapping("createproject/{id}")
    public String createproject(@ModelAttribute("project") Project project, int userid, String name) {
        repositoryDB.addProject(userid, name);
        return "redirect:/userProjects/{id}";
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

    @GetMapping("story/createstory/{boardid}")
    public String addStory(@PathVariable("boardid") int boardid, Model model, HttpSession session) {
        Story story = new Story();
        model.addAttribute("story", story);

        Board board = repositoryDB.getSpecificBoard(boardid);
        model.addAttribute("board", board);

        return isLogged(session) ? "createstory" : "index";
    }

    @PostMapping("story/createstory/{boardid}")
    public String addStory(@ModelAttribute("story") Story story, @PathVariable("boardid") int boardid) {
        repositoryDB.addStory(boardid, story);
        return "redirect:/storylist/{boardid}";
    }


    @GetMapping("story/{storyid}")
    public String getStory(@PathVariable("storyid") int storyid, Model model, HttpSession session) {
        Story story = repositoryDB.getSpecificStory(storyid);
        model.addAttribute("story", story);

        List<Task> tasks = repositoryDB.getTasks(storyid);
        model.addAttribute("tasks", tasks);

        return isLogged(session) ? "story" : "index";
    }

    @PostMapping("story/{storyid}")
    public String processForm(@PathVariable("storyid") int storyid, @RequestParam Map<String, Boolean> tasks, Model model) {
        Story story = repositoryDB.getSpecificStory(storyid);
        model.addAttribute("story", story);

        List<Task> taskslist = repositoryDB.getTasks(storyid);
        model.addAttribute("tasks", taskslist);

        model.addAttribute("tasks", tasks);
        return "story";
    }

    @GetMapping("story/createtask/{storyid}")
    public String addTask(@PathVariable("storyid") int storyid, Model model, HttpSession session) {
        Task task = new Task();
        model.addAttribute("task", task);

        Task task1 = repositoryDB.getSpecificTask(storyid);
        model.addAttribute("task1", task1);

        return isLogged(session) ? "createtask" : "index";
    }

    @PostMapping("story/createtask/{storyid}")
    public String addTask(@ModelAttribute("task") Task task, @PathVariable("storyid") int storyid) {
        repositoryDB.addTask(storyid, task);
        return "redirect:/story/{storyid}";
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
