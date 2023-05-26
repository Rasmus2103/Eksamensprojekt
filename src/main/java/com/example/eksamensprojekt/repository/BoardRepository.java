package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.Board;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository("board_DB")
public class BoardRepository implements IBoardRepository {
    @Override
    public List<Board> getBoards(int projectid) {
        List<Board> boards = new ArrayList<>();
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT * FROM board WHERE projectid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, projectid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String boardname = rs.getString("boardname");
                int boardid = rs.getInt("boardid");
                boards.add(new Board(boardid, boardname, projectid));
            }
            return boards;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Board getSpecificBoard(int boardid) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT boardname, boardid, projectid FROM board WHERE boardid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, boardid);
            ResultSet rs = ps.executeQuery();
            Board board = null;
            while (rs.next()) {
                String boardname = rs.getString("boardname");
                int projectid = rs.getInt("projectid");
                boardid = rs.getInt("boardid");
                board = new Board(boardid, boardname, projectid);
            }
            return board;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public int getProjectIdByBoardId(int boardId) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT projectid FROM board WHERE boardid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, boardId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("projectid");
            } else {
                throw new SQLException("No project found for board ID: " + boardId);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public int getBoardIdByProjectId(int projectid) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT boardid FROM board WHERE projectid = ? AND boardname = 'sprintboard'";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, projectid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("boardid");
            } else {
                throw new SQLException("No sprint board found for project ID: " + projectid);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public int getBacklogBoardIdByProjectId(int projectid) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT boardid FROM board WHERE projectid = ? AND boardname = 'backlog'";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, projectid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("boardid");
            } else {
                throw new SQLException("No sprint board found for project ID: " + projectid);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public int getHistoryBoardIdByProjectId(int projectId) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT boardid FROM board WHERE projectid = ? AND boardname = 'history board'";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, projectId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("boardid");
            } else {
                throw new SQLException("No history board found for project ID: " + projectId);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
