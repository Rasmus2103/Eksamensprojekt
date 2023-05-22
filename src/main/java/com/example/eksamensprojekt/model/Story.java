package com.example.eksamensprojekt.model;

import java.sql.Date;

public class Story {

    private int storyid;
    private String storyname;
    private String storydescription;
    private String acceptcriteria;
    private Date storydeadline;
    private int boardid;
    private boolean todo;
    private boolean inprogress;
    private boolean done;
    private boolean archived;

    public Story(){

    }

    public Story(int storyid, String storyname, String storydescription, String acceptcriteria, Date storydeadline, int boardid, boolean todo, boolean inprogress, boolean done, boolean archived){
        this.storyid = storyid;
        this.storyname = storyname;
        this.storydescription = storydescription;
        this.acceptcriteria = acceptcriteria;
        this.storydeadline = storydeadline;
        this.boardid = boardid;
        this.todo = todo;
        this.inprogress = inprogress;
        this.done = done;
        this.archived = archived;
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

    public Date getStorydeadline() {
        return storydeadline;
    }

    public void setStorydeadline(Date storydeadline) {
        this.storydeadline = storydeadline;
    }

    public int getBoardid() {
        return boardid;
    }

    public void setBoardid(int boardid) {
        this.boardid = boardid;
    }

    public boolean getTodo() {
        return todo;
    }

    public void setTodo(boolean todo) {
        this.todo = todo;
    }

    public boolean getInprogress() {
        return inprogress;
    }

    public void setInprogress(boolean inprogress) {
        this.inprogress = inprogress;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean getArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
