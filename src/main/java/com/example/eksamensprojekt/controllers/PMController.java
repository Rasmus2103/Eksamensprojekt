package com.example.eksamensprojekt.controllers;

import com.example.eksamensprojekt.model.User;
import com.example.eksamensprojekt.repository.RepositoryDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("")
public class PMController {

    @Autowired
    private RepositoryDB repositoryDB;

    public PMController(RepositoryDB repositoryDB) {
        this.repositoryDB = repositoryDB;
    }

    @GetMapping("")
    public String getUserById(@PathVariable int id) {
        return "index";
    }

    @GetMapping
    public int getUserid(String username){
        return repositoryDB.getUserid(username);
    }

}
