package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.*;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("eksamensprojekt_Stub")
public class RepositoryStub implements IRepositoryStub {

 // Stub data for testing
    private User testUser1 = new User(0, "Test", "Test", "Test");
    private User testUser2 = new User(1, "Test1", "Test1", "Test1");
    private User testUser3 = new User(2, "Test2", "Test2", "Test2");

    private List<User> usersStub = new ArrayList<>(List.of(testUser1, testUser2, testUser3));

    /*
    private List<Project> projectsStub = new ArrayList<>(List.of(new Project(0, "TestProject", "2023-10-10"),
            new Project(1, "TestProject1", 2023-10-10),
            new Project(2, "TestProject2", "2023-10-10")
            ));

     */

    private Map<Integer, ArrayList<User>> projectOverviewStub = Map.of(0, new ArrayList<>(List.of(testUser1)),
            1, new ArrayList<>(List.of(testUser1, testUser2)),
            2, new ArrayList<>(List.of()
            ));

    private List<Board> boardsStub = new ArrayList<>(List.of(new Board(0, "Backlog", 0),
            new Board(1, "Sprint", 0),
            new Board(2, "Backlog", 1),
            new Board(3, "Sprint", 1),
            new Board(4, "Backlog", 2),
            new Board(5, "Sprint", 2)
             ));

    /*private List<Story> storiesStub = new ArrayList<>(List.of(new Story(0, "TestStory", "TestStory", "TestStory", Date.valueOf("2021-05-05"), 0, false, false, false, false),
            new Story(1, "TestStory1", "TestStory1", "TestStory1", Date.valueOf("2021-05-05"), 0, false, false, false, false),
            new Story(2, "TestStory2", "TestStory2", "TestStory2", Date.valueOf("2021-05-05"), 1, false, false, false, false)
            ));*/

    private List<Task> tasksStub = new ArrayList<>(List.of(new Task(0, "TestTask", "TestTask", 10, 0),
            new Task(1, "TestTask1", "TestTask1", 7, 0),
            new Task(2, "TestTask2", "TestTask2", 3, 1),
            new Task(3, "TestTask3", "TestTask3", 1, 1)
            ));

    public RepositoryStub() {
    }

   @Override
   public List<User> getAllUsers() {
      return null;
   }

   @Override
    public User getUser(int userid) {
       User userToReturn = new User();
      for (User user : usersStub) {
         if (user.getUserid() == userid) {
            userToReturn = user;
         }
      }
      return userToReturn;
    }

    @Override
    public int getUserid(String username) {
        return 0;
    }

    @Override
    public boolean usernameExists(String username) {
        for (User user : usersStub) {
            if (user.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerUser(User user) {
          if (!usernameExists(user.getUserName())) {
             usersStub.add(user);
          }
    }

    @Override
    public void deleteUser(int userid) {
       usersStub.removeIf(user -> user.getUserid() == userid);
    }

    @Override
    public void deleteUserFromProject(int projectid, int userid){

    }

    @Override
    public void updateName(int userid, String name) {

    }

    @Override
    public void updateUsername(int userid, String username) {

    }

    @Override
    public void updatePassword(int userid, String password) {

    }

    @Override
    public List<Project> getProjects(int userid) {
        return null;
    }

    @Override
    public Project getSpecificProject(int projectid) {
        return null;
    }

    @Override
    public void addProject(int userid, String projectname) {

    }

    @Override
    public int getProjectId(String projectname) {
        return 0;
    }

    @Override
    public void addUserToProject(int userid, int projectid) {

    }

    @Override
    public List<String> getUserNamesByProjectId(int projectid) {
        return null;
    }

    @Override
    public void deleteProject(int projectid) {

    }

    @Override
    public void updateProjectName(int projectid, String projectname) {

    }

    @Override
    public List<Board> getBoards(int projectid) {
        return null;
    }

    @Override
    public Board getSpecificBoard(int boardid) {
        return null;
    }

    @Override
    public void addBoard(int projectid, String boardname) {

    }

    @Override
    public void deleteBoard(int boardid) {

    }

    @Override
    public void updateBoardName(int boardid, String boardname) {

    }

    @Override
    public List<Story> getStories(int boardid) {
        return null;
    }

    @Override
    public Story getSpecificStory(int storyid) {
        return null;
    }

    @Override
    public void addStory(int boardid, Story story) {

    }

    @Override
    public void deleteStory(int storyid) {

    }

    @Override
    public void updateStoryName(int storyid, String storyname) {

    }

    @Override
    public void updateStoryDescription(int storyid, String storydescription) {

    }

    @Override
    public void updateStoryAcceptcriteria(int storyid, String storyacceptcriteria) {

    }

    @Override
    public void updateStoryDeadline(int storyid, Date storydeadline) {

    }

    @Override
    public List<Task> getTasks(int storyid) {
        return null;
    }

    @Override
    public Task getSpecificTask(int taskid) {
        return null;
    }

    @Override
    public void addTask(int storyid, Task task) {

    }

    @Override
    public void deleteTask(int taskid) {

    }

    @Override
    public void updateTaskName(int taskid, String taskname) {

    }

    @Override
    public void updateTaskDescription(int taskid, String taskdescription) {

    }

    @Override
    public void updateTaskStorypoints(int taskid, int storypoints) {

    }

    @Override
    public List<Integer> getAllStoryPointsForBoard(int storyid) {
        return null;
    }

    @Override
    public int getSumOfStoryPointsForBoard(int storyid) {
        return 0;
    }
}
