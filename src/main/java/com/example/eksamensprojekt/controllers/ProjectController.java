package com.example.eksamensprojekt.controllers;
import com.example.eksamensprojekt.dto.ProjectDTOForm;
import com.example.eksamensprojekt.model.Board;
import com.example.eksamensprojekt.model.Project;
import com.example.eksamensprojekt.model.Story;
import com.example.eksamensprojekt.model.User;
import com.example.eksamensprojekt.repository.IProjectRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class ProjectController extends PMController {
    //private IProjectRepository projectRepository;

    public ProjectController(ApplicationContext context, @Value("project_DB") String impl) {
        //this.projectRepository = (IProjectRepository) context.getBean(impl);
    }

    @GetMapping("userProjects/{id}")
    public String getUserProjects(@PathVariable("id") int id, Model model, HttpSession session) {
        if (isLogged(session)) {
            User user = (User) session.getAttribute("user");
            if (user.getUserid() == id) {
                model.addAttribute("user", user);
                model.addAttribute("userid", user.getUserid());
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

        List<User> allusers = userRepository.getAllUsers();
        model.addAttribute("allusers", allusers);

        List<Board> boards = boardRepository.getBoards(projectid);
        model.addAttribute("boards", boards);

        List<String> users = projectRepository.getUserNamesByProjectId(projectid);
        model.addAttribute("users", users);

        return isLogged(session) ? "project" : "index";
    }

    @PostMapping("/project/{projectId}/{userId}/adduser")
    public String addUserToProject(@PathVariable("projectId") int projectId, @PathVariable("userId") int userId, @RequestParam("userIds") List<Integer> userIds) {
        for (int userid : userIds) {
            userRepository.addUserToProject(userid, projectId);
        }
        return "redirect:/project/" + projectId + "/" + userId;
    }


    @GetMapping("/leaveproject/{projectid}/{userid}")
    public String leaveproject(@PathVariable("projectid") int projectid, @PathVariable("userid") int userid) {
        userRepository.deleteUserFromProject(projectid, userid);
        return "redirect:/userProjects/" + userid;
    }

    @PostMapping("project/update/{projectid}/{userid}")
    public String updateProjectName(@ModelAttribute("project") Project project, @PathVariable("projectid") int projectid, @PathVariable("userid") int userid, Model model, HttpSession session) {
        if (project.getProjectname() != null) {
            projectRepository.updateProjectName(projectid, project.getProjectname());
            return "redirect:/project/" + projectid + "/" + userid;
        }
        model.addAttribute("wrongCredentials", true); /* TODO wrong credentials virker ikke */
        return "project/update/" + projectid + "/" + session.getAttribute("userid");
    }

    @PostMapping("project/updatedeadline/{projectid}")
    public String updateProjectdeadline(@ModelAttribute("project") Project project, @PathVariable("projectid") int projectid, HttpSession session) {
            projectRepository.updateProjectDeadline(projectid, project.getProjectdeadline());
           return "redirect:/project/" + projectid + "/" + session.getAttribute("userid");
    }

}
