package com.example.eksamensprojekt.dto;

import com.example.eksamensprojekt.model.User;

import java.sql.Date;
import java.util.List;

public class ProjectDTOForm {
    private int projectid;
    private String projectname;
    private Date projectdeadline;
    private List<String> userList;

    public ProjectDTOForm(int projectid, String projectname, List<String> userList, Date projectdeadline) {
        this.projectid = projectid;
        this.projectname = projectname;
        this.userList = userList;
        this.projectdeadline = projectdeadline;
    }

    public ProjectDTOForm() {

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

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public Date getProjectdeadline() {
        return projectdeadline;
    }

    public void setProjectdeadline(Date projectdeadline) {
        this.projectdeadline = projectdeadline;
    }


}
