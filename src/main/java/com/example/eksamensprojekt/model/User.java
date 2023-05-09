package com.example.eksamensprojekt.model;

public class User {
    private int userid;
    private String name;
    private String userName;
    private String password;

    public User(int userid, String name, String userName, String password) {
        this.userid = userid;
        this.name = name;
        this.userName = userName;
        this.password = password;
    }

    public User(String name) {
        this.name = name;
    }

    public User() {

    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
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
