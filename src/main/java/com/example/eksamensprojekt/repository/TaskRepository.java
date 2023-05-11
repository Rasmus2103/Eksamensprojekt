package com.example.eksamensprojekt.repository;
import com.example.eksamensprojekt.model.Task;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("eksamensprojekt_DB")
public class TaskRepository implements ITaskRepository {

    @Override
    public List<Task> getTasks(int storyid) {
        List<Task> tasks = new ArrayList<>();
        try{
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT * FROM task WHERE storyid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, storyid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int taskid = rs.getInt("taskid");
                String taskname = rs.getString("taskname");
                String taskdescription = rs.getString("taskdescription");
                int storypoints = rs.getInt("storypoints");
                tasks.add(new Task(taskid, taskname, taskdescription, storypoints, storyid));
            }
            return tasks;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Task getSpecificTask(int taskid) {
        try{
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT taskid, taskname, taskdescription, storypoints, storyid FROM task WHERE taskid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, taskid);
            ResultSet rs = ps.executeQuery();
            Task task = null;
            while(rs.next()){
                taskid = rs.getInt("taskid");
                String taskname = rs.getString("taskname");
                String taskdescription = rs.getString("taskdescription");
                int storypoints = rs.getInt("storypoints");
                int storydid = rs.getInt("storyid");
                task = new Task(taskid, taskname, taskdescription, storypoints, storydid);
            }
            return task;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addTask(int storyid, Task task) {
        try{
            Connection connection = ConnectionDB.connection();
            String SQL = "INSERT INTO task (taskname, taskdescription, storypoints, storyid) VALUES (?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.getTaskname());
            ps.setString(2, task.getTaskdescription());
            ps.setInt(3, task.getStorypoints());
            ps.setInt(4, storyid);
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTask(int taskid) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT taskid FROM task where taskid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, taskid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                taskid = rs.getInt("taskid");
            }
            SQL = "DELETE FROM task WHERE taskid = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, taskid);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTaskName(int taskid, String taskname) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "update task set taskname = ? where taskid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, taskname);
            ps.setInt(2, taskid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTaskDescription(int taskid, String taskdescription) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "update task set taskdescription = ? where taskid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, taskdescription);
            ps.setInt(2, taskid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTaskStorypoints(int taskid, int storypoints) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "update task set storypoints = ? where taskid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, storypoints);
            ps.setInt(2, taskid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
