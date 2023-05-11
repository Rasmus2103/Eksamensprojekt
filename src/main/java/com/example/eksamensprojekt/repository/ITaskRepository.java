package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.Task;

import java.util.List;

public interface ITaskRepository {
    List<Task> getTasks(int storyid);
    Task getSpecificTask(int taskid);
    void addTask(int storyid, Task task);
    void deleteTask(int taskid);
    void updateTaskName(int taskid, String taskname);
    void updateTaskDescription(int taskid, String taskdescription);
    void updateTaskStorypoints(int taskid, int storypoints);
}
