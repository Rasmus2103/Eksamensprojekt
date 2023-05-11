package com.example.eksamensprojekt.repository;
import com.example.eksamensprojekt.model.Board;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository("eksamensprojekt_DB")
public class BoardRepository implements IBoardRepository {
    @Override
    public List<Board> getBoards(int projectid) {
        List<Board> boards = new ArrayList<>();
        try{
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT * FROM board WHERE projectid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, projectid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String boardname = rs.getString("boardname");
                int boardid = rs.getInt("boardid");
                boards.add(new Board(boardid, boardname, projectid));
            }
            return boards;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Board getSpecificBoard(int boardid) {
        try{
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT boardname, boardid, projectid FROM board WHERE boardid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, boardid);
            ResultSet rs = ps.executeQuery();
            Board board = null;
            while(rs.next()){
                String boardname = rs.getString("boardname");
                int projectid = rs.getInt("projectid");
                boardid = rs.getInt("boardid");
                board = new Board(boardid, boardname, projectid);
            }
            return board;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
