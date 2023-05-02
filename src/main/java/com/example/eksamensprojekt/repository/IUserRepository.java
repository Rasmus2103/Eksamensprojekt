package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.User;
import com.example.eksamensprojekt.model.Project;

import java.util.List;

public interface IUserRepository {
    List<User> getUsers();
    User getUser(int userid);
    int getUserid(String username);
    boolean usernameExists(String username);
    void registerUser(User user);

    List<Project> getProjects(int userid);
    Project getSpecificProject(int projectid);
    void addProject(int userid, String projectname);
    void deleteProject(int projectid);



}
