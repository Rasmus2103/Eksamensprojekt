package com.example.eksamensprojekt.controllers;

import com.example.eksamensprojekt.dto.ProjectDTOForm;
import com.example.eksamensprojekt.model.Board;
import com.example.eksamensprojekt.model.Project;
import com.example.eksamensprojekt.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class ProjectController extends PMController {

    @GetMapping("userProjects/{id}")
    public String getUserProjects(@PathVariable("id") int id, Model model, HttpSession session) {
        if (isLogged(session)) {
            User user = (User) session.getAttribute("user");
                model.addAttribute("user", user);
                model.addAttribute("userid", user.getUserid());
                List<Project> projects = projectRepository.getProjects(id);
                model.addAttribute("projects", projects);
                ProjectDTOForm projectDTOForm = new ProjectDTOForm();
                model.addAttribute("projectDTO", projectDTOForm);
                return "userProjects";
        } else {
                return "index";
            }
    }

    @PostMapping("createproject/{id}")
    public String createproject(@ModelAttribute("projectDTO") ProjectDTOForm projectDto, @ModelAttribute("users") User users, @PathVariable("id") int id) {
        projectRepository.addProject(id, projectDto);
        return "redirect:/userProjects/" + id;
    }

    @GetMapping("userProjects/slet/{projectid}/{userid}")
    public String deleteProject(@PathVariable("projectid") int id, @PathVariable("userid") int userid, Model model) {
        projectRepository.deleteProject(id);
        return "redirect:/userProjects/" + userid;
    }

    @GetMapping("project/{projectid}/{userid}")
    public String getProject(@PathVariable("projectid") int projectid, @PathVariable("userid") int userid, Model model, HttpSession session) {
        Project project = projectRepository.getSpecificProject(projectid);
        model.addAttribute("project", project);

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
    public String updateProject(@ModelAttribute("project") Project project, @PathVariable("projectid") int projectid, @PathVariable("userid") int userid, Model model, HttpSession session) {
            projectRepository.updateProject(projectid, project);
            return "redirect:/project/" + projectid + "/" + userid;
    }

}
