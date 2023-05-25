package com.example.eksamensprojekt.repository;
import com.example.eksamensprojekt.model.Task;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("task_DB")
public class TaskRepository implements ITaskRepository {

    @Override
    public List<Task> getTasks(int storyid) {
        List<Task> tasks = new ArrayList<>();
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT * FROM task WHERE storyid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, storyid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Task task = new Task();
                int taskid = rs.getInt("taskid");
                task.setTaskid(taskid);
                String taskname = rs.getString("taskname");
                String taskdescription = rs.getString("taskdescription");
                int storypoints = rs.getInt("storypoints");
                boolean finished = rs.getBoolean("isfinished");
                tasks.add(new Task(taskid, taskname, taskdescription, storypoints, storyid, finished));
            }
            return tasks;
        } catch (SQLException e) {
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

    public void updateTask(int taskid, Task task) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "UPDATE task SET taskname = ?, taskdescription = ?, storypoints = ? WHERE taskid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, task.getTaskname());
            ps.setString(2, task.getTaskdescription());
            ps.setInt(3, task.getStorypoints());
            ps.setInt(4, taskid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public void updateTaskFinished(int taskId, boolean finished) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "UPDATE task SET isfinished = ? WHERE taskid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setBoolean(1, finished);
            ps.setInt(2, taskId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean areAllTasksFinished(int storyId) {
        List<Task> tasks = getTasks(storyId);

        for (Task task : tasks) {
            if (!task.isFinished()) {
                return false;
            }
        }

        return true;
    }


}
