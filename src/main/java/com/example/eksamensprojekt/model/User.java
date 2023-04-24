package com.example.eksamensprojekt.model;

public class User {
    private int userid;
    private String userName;
    private int password;

    public User(int userid, String userName, int password) {
        this.userid = userid;
        this.userName = userName;
        this.password = password;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }
}
