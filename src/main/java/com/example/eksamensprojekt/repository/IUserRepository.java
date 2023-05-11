package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.User;

import java.util.List;

public interface IUserRepository {
    List<User> getAllUsers();
    User getUser(int userid);
    int getUserid(String username);
    boolean usernameExists(String username);
    void registerUser(User user);
    void deleteUser(int userid);
    void addUserToProject(int userid, int projectid);
    void deleteUserFromProject(int projectid, int userid);
    void updateName(int userid, String name);
    void updateUsername(int userid, String username);
    void updatePassword(int userid, String password);
}
