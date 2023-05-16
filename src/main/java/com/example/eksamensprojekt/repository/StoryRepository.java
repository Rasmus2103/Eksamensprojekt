package com.example.eksamensprojekt.repository;
import com.example.eksamensprojekt.model.Story;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("story_DB")
public class StoryRepository implements IStoryRepository {

    @Override
    public List<Story> getStories(int boardid) {
        List<Story> stories = new ArrayList<>();
        try{
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT * FROM story WHERE boardid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, boardid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int storyid = rs.getInt("storyid");
                String storyname = rs.getString("storyname");
                String storydescription = rs.getString("storydescription");
                String acceptcriteria = rs.getString("acceptcriteria");
                Date storydeadline = rs.getDate("storydeadline");
                stories.add(new Story(storyid, storyname, storydescription, acceptcriteria, storydeadline, boardid));
            }
            return stories;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Story getSpecificStory(int storyid) {
        try{
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT storyid, storyname, storydescription, acceptcriteria, storydeadline, boardid FROM story WHERE storyid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, storyid);
            ResultSet rs = ps.executeQuery();
            Story story = null;
            while(rs.next()){
                storyid = rs.getInt("storyid");
                String storyname = rs.getString("storyname");
                String storydescription = rs.getString("storydescription");
                String acceptcriteria = rs.getString("acceptcriteria");
                Date storydeadline = rs.getDate("storydeadline");
                int boardid = rs.getInt("boardid");
                story = new Story(storyid, storyname, storydescription, acceptcriteria, storydeadline, boardid);
            }
            return story;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addStory(int boardid, Story story) {
        try{
            Connection connection = ConnectionDB.connection();
            String SQL = "INSERT INTO story (storyname, storydescription, acceptcriteria, storydeadline, boardid) VALUES (?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, story.getStoryname());
            ps.setString(2, story.getStorydescription());
            ps.setString(3, story.getAcceptcriteria());
            ps.setDate(4, story.getStorydeadline());
            ps.setInt(5, boardid);
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteStory(int storyid) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT storyid FROM story WHERE storyid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, storyid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                storyid = rs.getInt("storyid");
            }
            SQL = "DELETE FROM task WHERE storyid = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, storyid);
            ps.executeUpdate();

            SQL = "DELETE FROM story WHERE storyid = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, storyid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStoryName(int storyid, String storyname) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "update story set storyname = ? where storyid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, storyname);
            ps.setInt(2, storyid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStoryDescription(int storyid, String storydescription) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "update story set storydescription = ? where storyid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, storydescription);
            ps.setInt(2, storyid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStoryAcceptcriteria(int storyid, String storyacceptcriteria) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "update story set acceptcriteria = ? where storyid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, storyacceptcriteria);
            ps.setInt(2, storyid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStoryDeadline(int storyid, Date storydeadline) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "update story set storydeadline = ? where storyid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setDate(1, (java.sql.Date) storydeadline);
            ps.setInt(2, storyid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<String> getUserNamesByStoryId(int storyid) {
        List<String> userNames = new ArrayList<>();
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT story.storyname, user.username " +
            "FROM story " +
            "JOIN storyuser ON story.storyid = storyuser.storyid " +
            "JOIN user ON user.userid = storyuser.userid " +
            "WHERE story.storyid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, storyid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String name = rs.getString("username");
                userNames.add(name);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return userNames;
    }

    public void addUserToStory(int storyid, int userid) {
        try {
            Connection connection = ConnectionDB.connection();
            String checkSQL = "SELECT * FROM storyuser WHERE storyid = ? AND userid = ?";
            PreparedStatement checkPs = connection.prepareStatement(checkSQL);
            checkPs.setInt(1, storyid);
            checkPs.setInt(2, userid);
            ResultSet rs = checkPs.executeQuery();
            if(rs.next()) {
                System.out.println("User has already been asigned to story");
                return;
            }

            String SQL = "INSERT INTO storyuser (storyid, userid) VALUES (?,?)";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, storyid);
            ps.setInt(2, userid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void moveStoryToBoard(int storyid, int boardid) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "UPDATE story SET boardid = ? WHERE storyid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, boardid);
            ps.setInt(2, storyid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public List<Integer> getAllStoryPoints(int storyid) {
        List<Integer> storyPoints = new ArrayList<>();
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT storypoints FROM task WHERE storyid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, storyid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                storyPoints.add(rs.getInt("storypoints"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return storyPoints;
    }

    public int getSumOfStoryPoints(int storyid) {
        List<Integer> storyPoints = getAllStoryPoints(storyid);
        int sum = 0;
        for (int points : storyPoints) {
            sum += points;
        }
        return sum;
    }

}
