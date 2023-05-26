package com.example.eksamensprojekt.model;

public class Board {

    private int boardid;
    private String boardname;
    private int projectid;

    public Board(int boardid, String boardname, int projectid) {
        this.boardid = boardid;
        this.boardname = boardname;
        this.projectid = projectid;
    }

    public Board() {

    }

    public int getBoardid() {
        return boardid;
    }

    public void setBoardid(int boardid) {
        this.boardid = boardid;
    }

    public String getBoardname() {
        return boardname;
    }

    public void setBoardname(String boardname) {
        this.boardname = boardname;
    }

    public int getProjectid() {
        return projectid;
    }

    public void setProjectid(int projectid) {
        this.projectid = projectid;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + boardid +
                ", name='" + boardname + '\'' +
                ", projectId=" + projectid +
                '}';
    }
}
