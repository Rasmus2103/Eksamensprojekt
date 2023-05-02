package com.example.eksamensprojekt.model;

public class User {
    private int userid;
    private String Name;
    private String userName;
    private String password;

    public User(int userid, String Name, String userName, String password) {
        this.userid = userid;
        this.Name = Name;
        this.userName = userName;
        this.password = password;
    }

    public User() {

    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
