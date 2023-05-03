package com.example.eksamensprojekt.controllers;

import com.example.eksamensprojekt.model.Project;
import com.example.eksamensprojekt.model.User;
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
        return "redirect:/projectManagement";
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
                return "redirect:/projectManagement";
            }
        } else {
            return "index";
        }
    }

    @GetMapping("project/{userid}/{projectid}")
    public String getProject(@PathVariable("userid") int userid, @PathVariable("projectid") int projectid, Model model, HttpSession session) {
        User user = repositoryDB.getUser(userid);
        model.addAttribute("user", user);

        Project project = repositoryDB.getSpecificProject(projectid);
        model.addAttribute("project", project);

        return isLogged(session) ? "project" : "index";
    }

}
