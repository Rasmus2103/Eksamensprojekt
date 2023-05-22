package com.example.eksamensprojekt.model;

import java.sql.Date;

public class Project {

    private int projectid;
    private String projectname;
    private Date projectdeadline;

    public Project(int projectid, String projectname, Date projectdeadline){
        this.projectid = projectid;
        this.projectname = projectname;
        this.projectdeadline = projectdeadline;
    }

    public Project(){

    }

    public int getProjectid() {
        return projectid;
    }

    public void setProjectid(int projectid) {
        this.projectid = projectid;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public Date getProjectdeadline() {
        return projectdeadline;
    }

    public void setProjectdeadline(Date projectdeadline) {
        this.projectdeadline = projectdeadline;
    }


}
