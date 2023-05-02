package com.example.eksamensprojekt.model;

import java.sql.Date;

public class Story {

    private int storyid;
    private String storyname;
    private String storydescription;
    private String acceptcriteria;
    private Date deadline;
    private int boardid;

    public Story(){

    }

    public Story(int storyid, String storyname, String storydescription, String acceptcriteria, Date deadline, int boardid){
        this.storyid = storyid;
        this.storyname = storyname;
        this.storydescription = storydescription;
        this.acceptcriteria = acceptcriteria;
        this.deadline = deadline;
        this.boardid = boardid;
    }

    public int getStoryid() {
        return storyid;
    }

    public void setStoryid(int storyid) {
        this.storyid = storyid;
    }

    public String getStoryname() {
        return storyname;
    }

    public void setStoryname(String storyname) {
        this.storyname = storyname;
    }

    public String getStorydescription() {
        return storydescription;
    }

    public void setStorydescription(String storydescription) {
        this.storydescription = storydescription;
    }

    public String getAcceptcriteria() {
        return acceptcriteria;
    }

    public void setAcceptcriteria(String acceptcriteria) {
        this.acceptcriteria = acceptcriteria;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public int getBoardid() {
        return boardid;
    }

    public void setBoardid(int boardid) {
        this.boardid = boardid;
    }
}
