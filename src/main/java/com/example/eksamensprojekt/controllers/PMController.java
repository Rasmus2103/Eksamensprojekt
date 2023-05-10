package com.example.eksamensprojekt.controllers;

import com.example.eksamensprojekt.dto.ProjectDTOForm;
import com.example.eksamensprojekt.model.*;
import com.example.eksamensprojekt.repository.IRepositoryDB;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class PMController {
    private IRepositoryDB repositoryDB;

    public PMController(ApplicationContext context, @Value("eksamensprojekt_DB") String impl) {
        this.repositoryDB =(IRepositoryDB) context.getBean(impl);
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

    @GetMapping("register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            repositoryDB.registerUser(user);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("usernameExists", true);
            return "register";
        }
    }

    @GetMapping("userProjects/{id}")
    public String getUserProjects(@PathVariable("id") int id, Model model, HttpSession session) {
        if(isLogged(session)) {
            User user = (User) session.getAttribute("user");
            if (user.getUserid() == id) {
                model.addAttribute("user", user);

                List<Project> projects = repositoryDB.getProjects(id);
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
    public String createProject(@PathVariable("id") int id, Model model, HttpSession session) {
        ProjectDTOForm projectDTOForm = new ProjectDTOForm();
        model.addAttribute("projectDTO", projectDTOForm);

        List<String> users = repositoryDB.getAllUsers();
        model.addAttribute("users", users);

        User user = repositoryDB.getUser(id);
        model.addAttribute("user", user);
        return isLogged(session) ? "createproject" : "index";
    }

    @PostMapping("createproject/{id}")
    public String createproject(@ModelAttribute("projectDTO") ProjectDTOForm projectDto, @ModelAttribute("users") User users, @PathVariable("id") int id) {
        repositoryDB.addProject(id, projectDto.getProjectname());
        return "redirect:/userProjects/" + id;
    }

    @GetMapping("userProjects/slet/{projectid}/{userid}")
    public String deleteProject(@PathVariable("projectid") int id, @PathVariable("userid") int userid, Model model) {
        repositoryDB.deleteProject(id);

        List<Project> projects = repositoryDB.getProjects(id);
        model.addAttribute("projects", projects);

        return "redirect:/userProjects/" + userid;
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

    @GetMapping("project/update/{projectid}")
    public String updateProjectName(@PathVariable("projectid") int projectid, Model model, HttpSession session) {
        Project project = repositoryDB.getSpecificProject(projectid);
        model.addAttribute("project", project);
        return isLogged(session) ? "updateproject" : "index";
    }

    @PostMapping("project/update/{projectid}")
    public String updateProjectName(@ModelAttribute("project") Project project, @PathVariable("projectid") int projectid, Model model) {
        if (project.getProjectname() != null){
            repositoryDB.updateProjectName(projectid, project.getProjectname());
            return "redirect:/project/" + projectid;
        }
        model.addAttribute("wrongCredentials", true); /* TODO wrong credentials virker ikke */
        return "project/update/" + projectid;
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

    @GetMapping("story/slet/{boardid}/{storyid}")
    public String deleteStory(@PathVariable ("boardid") int boardid, @PathVariable("storyid") int storyid, Model model) {
        repositoryDB.deleteStory(storyid);

        List<Story> stories = repositoryDB.getStories(boardid);
        model.addAttribute("story", stories);
        return "redirect:/storylist/" + boardid;
    }

    @GetMapping("story/{storyid}")
    public String getStory(@PathVariable("storyid") int storyid, Model model, HttpSession session) {
        Story story = repositoryDB.getSpecificStory(storyid);
        model.addAttribute("story", story);

        List<Task> tasks = repositoryDB.getTasks(storyid);
        model.addAttribute("tasks", tasks);

        return isLogged(session) ? "story" : "index";
    }

    @GetMapping("story/update/{storyid}")
    public String updateStory(@PathVariable("storyid") int storyid, Model model, HttpSession session) {
        Story story = repositoryDB.getSpecificStory(storyid);
        model.addAttribute("story", story);
        return isLogged(session) ? "updatestory" : "index";
    }

    @PostMapping("story/update/{storyid}/{boardid}")
    public String updateStory(@ModelAttribute("story") Story story, @PathVariable("storyid") int storyid, @PathVariable("boardid") int boardid) {
        repositoryDB.updateStoryName(storyid, story.getStoryname());
        repositoryDB.updateStoryDescription(storyid, story.getStorydescription());
        repositoryDB.updateStoryAcceptcriteria(storyid, story.getAcceptcriteria());
        repositoryDB.updateStoryDeadline(storyid, story.getDeadline());
        return "redirect:/storylist/" + boardid;
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

    @GetMapping("storyid/slet/{storyid}/{taskid}")
    public String deleteTask(@PathVariable("storyid") int storyid, @PathVariable("taskid") int taskid, Model model) {
        repositoryDB.deleteTask(taskid);

        Story story = repositoryDB.getSpecificStory(storyid);
        model.addAttribute("story", story);
        return "redirect:/story/" + storyid;
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

    @GetMapping("task/update/{taskid}")
    public String updateTask(@PathVariable("taskid") int taskid, Model model, HttpSession session) {
        Task task = repositoryDB.getSpecificTask(taskid);
        model.addAttribute("task", task);
        return isLogged(session) ? "updatetask" : "index";
    }

    @PostMapping("task/update/{taskid}/{storyid}")
    public String updateTask(@ModelAttribute("task") Task task, @PathVariable("taskid") int taskid, @PathVariable("storyid") int storyid) {
        repositoryDB.updateTaskName(taskid, task.getTaskname());
        repositoryDB.updateTaskDescription(taskid, task.getTaskdescription());
        repositoryDB.updateTaskStorypoints(taskid, task.getStorypoints());

        return "redirect:/story/" + storyid;
    }

    @GetMapping("account/{userid}")
    public String getAccount(@PathVariable("userid") int userid, Model model, HttpSession session) {
        if (isLogged(session)) {
            User user = (User) session.getAttribute("user");
            if (user.getUserid() == userid) {
                model.addAttribute("user", user);
                return "account";
            } else {
                return "redirect:/userProjects";
            }
        } else {
            return "index";
        }
    }

    @GetMapping("account/delete/{userid}")
    public String deleteAccount(@PathVariable("userid") int userid, HttpSession session) {
        repositoryDB.deleteUser(userid);
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("account/update/{userid}")
    public String updateAccount(@PathVariable("userid") int userid, @ModelAttribute("user") User user, HttpSession session) {
        repositoryDB.updateUsername(userid, user.getUserName());
        repositoryDB.updateName(userid, user.getname());
        repositoryDB.updatePassword(userid, user.getPassword());

        session.setAttribute("user", user);

        return "redirect:/account/" + userid;
    }

}
