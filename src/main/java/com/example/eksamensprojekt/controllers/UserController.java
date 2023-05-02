package com.example.eksamensprojekt.controllers;

import com.example.eksamensprojekt.model.User;
import com.example.eksamensprojekt.repository.RepositoryDB;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
public class UserController {
    private RepositoryDB repositoryDB;

    public UserController(RepositoryDB repositoryDB) {
        this.repositoryDB = repositoryDB;
    }

    @GetMapping("/")
    public String getUsers(Model model) {
      //List<User> users = repositoryDB.getUsers();
      //model.addAttribute("user", users);
      return "index";
    }
}
