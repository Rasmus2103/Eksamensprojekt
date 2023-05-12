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
    private IUserRepository userRepository;
    private IProjectRepository projectRepository;
    private IBoardRepository boardRepository;
    private IStoryRepository storyRepository;
    private ITaskRepository taskRepository;

    public PMController(IUserRepository userRepository, IProjectRepository projectRepository, IBoardRepository boardRepository, IStoryRepository storyRepository, ITaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.boardRepository = boardRepository;
        this.storyRepository = storyRepository;
        this.taskRepository = taskRepository;
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
        User user = userRepository.getUser(userRepository.getUserid(username));
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
            userRepository.registerUser(user);
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
                List<Project> projects = projectRepository.getProjects(id);
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

        User user = userRepository.getUser(id);
        model.addAttribute("user", user);
        return isLogged(session) ? "createproject" : "index";
    }

    @PostMapping("createproject/{id}")
    public String createproject(@ModelAttribute("projectDTO") ProjectDTOForm projectDto, @ModelAttribute("users") User users, @PathVariable("id") int id) {
        projectRepository.addProject(id, projectDto.getProjectname());
        return "redirect:/userProjects/" + id;
    }

    @GetMapping("userProjects/slet/{projectid}/{userid}")
    public String deleteProject(@PathVariable("projectid") int id, @PathVariable("userid") int userid, Model model) {
        projectRepository.deleteProject(id);

        List<Project> projects = projectRepository.getProjects(id);
        model.addAttribute("projects", projects);

        return "redirect:/userProjects/" + userid;
    }

    @GetMapping("project/{projectid}/{userid}")
    public String getProject(@PathVariable("projectid") int projectid, @PathVariable("userid") int userid, Model model, HttpSession session) {
        Project project = projectRepository.getSpecificProject(projectid);
        model.addAttribute("project", project);

        int boardid = 0;
        List<Story> stories = storyRepository.getStories(boardid);
        model.addAttribute("stories", stories);

        User user = userRepository.getUser(userid);
        model.addAttribute("user", user);

        List<Board> boards = boardRepository.getBoards(projectid);
        model.addAttribute("boards", boards);

        List<String> users = projectRepository.getUserNamesByProjectId(projectid);
        model.addAttribute("users", users);

        return isLogged(session) ? "project" : "index";
    }

    @GetMapping("/project/{projectId}/{userId}/addusers")
    public String showAddUserForm(@PathVariable("projectId") int projectId, @PathVariable("userId") int userId, Model model) {
        Project project = projectRepository.getSpecificProject(projectId);
        List<User> users = userRepository.getAllUsers();

        model.addAttribute("project", project);
        model.addAttribute("users", users);

        return "addusers";
    }

    @PostMapping("/project/{projectId}/{userId}/adduser")
    public String addUserToProject(@PathVariable("projectId") int projectId, @PathVariable("userId") int userId, @RequestParam("userIds") List<Integer> userIds) {
        for (int userid : userIds) {
            userRepository.addUserToProject(userid, projectId);
        }
        return "redirect:/project/" + projectId + "/" + userId;
    }


    @GetMapping("/leaveproject/{projectid}/{userid}")
    public String leaveproject(@PathVariable("projectid") int projectid, @PathVariable("userid") int userid){
        userRepository.deleteUserFromProject(projectid,userid);
        return "redirect:/userProjects/" + userid;
    }


    @GetMapping("project/update/{projectid}/{userid}")
    public String updateProjectName(@PathVariable("projectid") int projectid, @PathVariable("userid") int userid, Model model, HttpSession session) {
        Project project = projectRepository.getSpecificProject(projectid);
        model.addAttribute("project", project);

        User user = userRepository.getUser(userid);
        model.addAttribute("user", user);
        return isLogged(session) ? "updateproject" : "index";
    }

    @PostMapping("project/update/{projectid}/{userid}")
    public String updateProjectName(@ModelAttribute("project") Project project, @PathVariable("projectid") int projectid, @PathVariable("userid") int userid, Model model) {
        if (project.getProjectname() != null){
            projectRepository.updateProjectName(projectid, project.getProjectname());
            return "redirect:/project/" + projectid + "/" + userid;
        }
        model.addAttribute("wrongCredentials", true); /* TODO wrong credentials virker ikke */
        return "project/update/" + projectid;
    }

    @GetMapping("storylist/{boardid}")
    public String getStories(@PathVariable("boardid") int boardid, Model model, HttpSession session) {
        List<Story> stories = storyRepository.getStories(boardid);
        model.addAttribute("stories", stories);

        Object userid = session.getAttribute("userid");
        model.addAttribute("userid", userid);

        return isLogged(session) ? "storylist" : "index";
    }

    @GetMapping("story/createstory/{boardid}")
    public String addStory(@PathVariable("boardid") int boardid, Model model, HttpSession session) {
        Story story = new Story();
        model.addAttribute("story", story);

        Board board = boardRepository.getSpecificBoard(boardid);
        model.addAttribute("board", board);

        return isLogged(session) ? "createstory" : "index";
    }

    @PostMapping("story/createstory/{boardid}")
    public String addStory(@ModelAttribute("story") Story story, @PathVariable("boardid") int boardid) {
        storyRepository.addStory(boardid, story);
        return "redirect:/storylist/{boardid}";
    }

    @GetMapping("story/slet/{boardid}/{storyid}")
    public String deleteStory(@PathVariable ("boardid") int boardid, @PathVariable("storyid") int storyid, Model model) {
        storyRepository.deleteStory(storyid);

        List<Story> stories = storyRepository.getStories(boardid);
        model.addAttribute("story", stories);
        return "redirect:/storylist/" + boardid;
    }

    @GetMapping("story/{storyid}")
    public String getStory(@PathVariable("storyid") int storyid, Model model, HttpSession session) {
        Story story = storyRepository.getSpecificStory(storyid);
        model.addAttribute("story", story);

        List<Task> tasks = taskRepository.getTasks(storyid);
        model.addAttribute("tasks", tasks);

        int totalStoryPoints = storyRepository.getSumOfStoryPoints(storyid);
        model.addAttribute("totalStoryPoints", totalStoryPoints);

        return isLogged(session) ? "story" : "index";
    }

    @GetMapping("story/update/{storyid}")
    public String updateStory(@PathVariable("storyid") int storyid, Model model, HttpSession session) {
        Story story = storyRepository.getSpecificStory(storyid);
        model.addAttribute("story", story);
        return isLogged(session) ? "updatestory" : "index";
    }

    @PostMapping("story/update/{storyid}/{boardid}")
    public String updateStory(@ModelAttribute("story") Story story, @PathVariable("storyid") int storyid, @PathVariable("boardid") int boardid) {
        storyRepository.updateStoryName(storyid, story.getStoryname());
        storyRepository.updateStoryDescription(storyid, story.getStorydescription());
        storyRepository.updateStoryAcceptcriteria(storyid, story.getAcceptcriteria());
        storyRepository.updateStoryDeadline(storyid, story.getDeadline());
        return "redirect:/storylist/" + boardid;
    }


    @PostMapping("story/{storyid}")
    public String processForm(@PathVariable("storyid") int storyid, @RequestParam Map<String, Boolean> tasks, Model model) {
        Story story = storyRepository.getSpecificStory(storyid);
        model.addAttribute("story", story);

        List<Task> taskslist = taskRepository.getTasks(storyid);
        model.addAttribute("tasks", taskslist);

        model.addAttribute("tasks", tasks);
        return "story";
    }

    @GetMapping("storyid/slet/{storyid}/{taskid}")
    public String deleteTask(@PathVariable("storyid") int storyid, @PathVariable("taskid") int taskid, Model model) {
        taskRepository.deleteTask(taskid);

        Story story = storyRepository.getSpecificStory(storyid);
        model.addAttribute("story", story);
        return "redirect:/story/" + storyid;
    }

    @GetMapping("board/{boardid}")
    public String getBoard(@PathVariable("boardid") int boardid, Model model, HttpSession session) {
        Board board = boardRepository.getSpecificBoard(boardid);
        model.addAttribute("board", board);

        return isLogged(session) ? "board" : "index";
    }

    @GetMapping("task/{taskid}")
    public String getTask(@PathVariable("taskid") int taskid, Model model, HttpSession session) {
        Task task = taskRepository.getSpecificTask(taskid);
        model.addAttribute("task", task);

        return isLogged(session) ? "task" : "index";
    }

    @GetMapping("story/createtask/{storyid}")
    public String addTask(@PathVariable("storyid") int storyid, Model model, HttpSession session) {
        Task task = new Task();
        model.addAttribute("task", task);

        Task task1 = taskRepository.getSpecificTask(storyid);
        model.addAttribute("task1", task1);

        return isLogged(session) ? "createtask" : "index";
    }

    @PostMapping("story/createtask/{storyid}")
    public String addTask(@ModelAttribute("task") Task task, @PathVariable("storyid") int storyid) {
        taskRepository.addTask(storyid, task);
        return "redirect:/story/{storyid}";
    }

    @GetMapping("task/update/{taskid}")
    public String updateTask(@PathVariable("taskid") int taskid, Model model, HttpSession session) {
        Task task = taskRepository.getSpecificTask(taskid);
        model.addAttribute("task", task);
        return isLogged(session) ? "updatetask" : "index";
    }

    @PostMapping("task/update/{taskid}/{storyid}")
    public String updateTask(@ModelAttribute("task") Task task, @PathVariable("taskid") int taskid, @PathVariable("storyid") int storyid) {
        taskRepository.updateTaskName(taskid, task.getTaskname());
        taskRepository.updateTaskDescription(taskid, task.getTaskdescription());
        taskRepository.updateTaskStorypoints(taskid, task.getStorypoints());

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
        userRepository.deleteUser(userid);
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("account/update/{userid}")
    public String updateAccount(@PathVariable("userid") int userid, @ModelAttribute("user") User user, HttpSession session) {
        userRepository.updateUsername(userid, user.getUserName());
        userRepository.updateName(userid, user.getname());
        userRepository.updatePassword(userid, user.getPassword());

        session.setAttribute("user", user);

        return "redirect:/account/" + userid;
    }

}
