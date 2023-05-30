package com.example.eksamensprojekt.model;

public class Task {

    private int taskid;
    private String taskname;
    private String taskdescription;
    private int storypoints;
    private int storyid;
    private boolean finished;

    public Task(int taskid, String taskname, String taskdescription, int storypoints, int storyid) {
        this.taskid = taskid;
        this.taskname = taskname;
        this.taskdescription = taskdescription;
        this.storypoints = storypoints;
        this.storyid = storyid;
    }

    public Task(int taskid, String taskname, String taskdescription, int storypoints, int storyid, boolean finished) {
        this.taskid = taskid;
        this.taskname = taskname;
        this.taskdescription = taskdescription;
        this.storypoints = storypoints;
        this.storyid = storyid;
        this.finished = finished;
    }

    public Task() {

    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getTaskdescription() {
        return taskdescription;
    }

    public void setTaskdescription(String taskdescription) {
        this.taskdescription = taskdescription;
    }

    public int getStorypoints() {
        return storypoints;
    }

    public void setStorypoints(int storypoints) {
        this.storypoints = storypoints;
    }

    public int getStoryid() {
        return storyid;
    }

    public void setStoryid(int storyid) {
        this.storyid = storyid;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
