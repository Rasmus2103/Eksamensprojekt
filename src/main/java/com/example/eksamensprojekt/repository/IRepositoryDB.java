package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.*;

import java.sql.Date;
import java.util.List;

public interface IRepositoryDB {
    List<User> getAllUsers();
    User getUser(int userid);
    int getUserid(String username);
    boolean usernameExists(String username);
    void registerUser(User user);
    void deleteUser(int userid);
    void deleteUserFromProject(int projectid, int userid);
    void updateName(int userid, String name);
    void updateUsername(int userid, String username);
    void updatePassword(int userid, String password);

    List<Project> getProjects(int userid);
    Project getSpecificProject(int projectid);
    void addProject(int userid, String projectname);
    int getProjectId(String projectname);
    void addUserToProject(int userid, int projectid);
    void deleteProject(int projectid);
    void updateProjectName(int projectid, String projectname);

     List<Board> getBoards(int projectid);
    Board getSpecificBoard(int boardid);
    void addBoard(int projectid, String boardname);
    void deleteBoard(int boardid);
    void updateBoardName(int boardid, String boardname);

    List<Story> getStories(int boardid);
    Story getSpecificStory(int storyid);
    void addStory(int boardid, Story story);
    void deleteStory(int storyid);
    void updateStoryName(int storyid, String storyname);
    void updateStoryDescription(int storyid, String storydescription);
    void updateStoryAcceptcriteria(int storyid, String storyacceptcriteria);
    void updateStoryDeadline(int storyid, Date storydeadline);

    List<Task> getTasks(int storyid);
    Task getSpecificTask(int taskid);
    void addTask(int storyid, Task task);
    void deleteTask(int taskid);
    void updateTaskName(int taskid, String taskname);
    void updateTaskDescription(int taskid, String taskdescription);
    void updateTaskStorypoints(int taskid, int storypoints);

    List<Integer>getAllStoryPointsForBoard(int storyid);
    int getSumOfStoryPointsForBoard(int storyid);

}
