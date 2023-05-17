package com.example.eksamensprojekt.repository;
import com.example.eksamensprojekt.dto.ProjectDTOForm;
import com.example.eksamensprojekt.model.Project;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository("project_DB")
public class ProjectRepository implements IProjectRepository {

    @Override
    public List<Project> getProjects(int userid) {
        List<Project> projects = new ArrayList<>();
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT p.projectname, p.projectid, p.projectdeadline FROM project p " +
                    "JOIN userproject up ON p.projectid = up.projectid " +
                    "WHERE up.userid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String projectname = rs.getString("projectname");
                int projectid = rs.getInt("projectid");
                Date projectdeadline = rs.getDate("projectdeadline");
                projects.add(new Project(projectid, projectname, projectdeadline));
            }
            return projects;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Project getSpecificProject(int projectid) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT projectname, projectid, projectdeadline FROM project WHERE projectid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, projectid);
            ResultSet rs = ps.executeQuery();
            Project project = null;
            while (rs.next()) {
                String projectname = rs.getString("projectname");
                projectid = rs.getInt("projectid");
                Date projectdeadline = rs.getDate("projectdeadline");
                project = new Project(projectid, projectname, projectdeadline);
            }
            return project;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addProject(int userid, ProjectDTOForm project) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "INSERT INTO project (projectname, projectdeadline) VALUES (?,?)";
            PreparedStatement ps = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, project.getProjectname());
            ps.setDate(2, (java.sql.Date) project.getProjectdeadline());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int projectid = rs.getInt(1);
                addBoard(projectid, "backlog");
                addBoard(projectid, "sprintboard");
                addBoard(projectid, "history board");
                addUserToProject(userid, projectid);
            } else {
                throw new SQLException("Creating project failed, no ID obtained.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<String> getUserNamesByProjectId(int projectid) {
        List<String> userNames = new ArrayList<>();
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT u.name " +
                    "FROM user AS u " +
                    "JOIN userproject AS up ON u.userid = up.userid " +
                    "JOIN project AS p ON p.projectid = up.projectid " +
                    "WHERE p.projectid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, projectid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String name = rs.getString("name");
                userNames.add(name);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return userNames;
    }

    @Override
    public void deleteProject(int projectid) {
        try {
            Connection connection = ConnectionDB.connection();
            int boardid;
            String SQL = "SELECT boardid FROM board WHERE projectid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, projectid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                boardid = rs.getInt("boardid");
                deleteBoard(boardid);
            }

            String deleteUserProjectSQL = "DELETE FROM userproject WHERE projectid = ?";
            PreparedStatement psDeleteUserProject = connection.prepareStatement(deleteUserProjectSQL);
            psDeleteUserProject.setInt(1, projectid);
            psDeleteUserProject.executeUpdate();

            SQL = "DELETE FROM project WHERE projectid = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, projectid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateProjectName(int projectid, String projectname) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "update project set projectname = ? where projectid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, projectname);
            ps.setInt(2, projectid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateProjectDeadline(int projectid, Date projectdeadline) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "update project set projectdeadline = ? where projectid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setDate(1, (java.sql.Date) projectdeadline);
            ps.setInt(2, projectid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // OBS! de 3 nederste metoder bliver benyttet som hjælpe metoder i denne klasse
    @Override
    public void addUserToProject(int userid, int projectid) {
        try {
            Connection connection = ConnectionDB.connection();
            String checkSQL = "SELECT * FROM userproject WHERE userid = ? AND projectid = ?";
            PreparedStatement checkPs = connection.prepareStatement(checkSQL);
            checkPs.setInt(1, userid);
            checkPs.setInt(2, projectid);
            ResultSet rs = checkPs.executeQuery();
            if(rs.next()) {
                System.out.println("User has already been assigned");
                return;
            }

            String SQL = "INSERT INTO userproject (userid, projectid) VALUES (?,?)";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, userid);
            ps.setInt(2, projectid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addBoard(int projectid, String boardname) {
        try{
            Connection connection = ConnectionDB.connection();
            String SQL = "INSERT INTO board (boardname, projectid) VALUES (?,?)";
            PreparedStatement ps = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, boardname);
            ps.setInt(2, projectid);
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteBoard(int boardid) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT boardid FROM board WHERE boardid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, boardid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boardid = rs.getInt("boardid");
            }
            int storyid;
            String SQL2 = "SELECT storyid from story WHERE boardid = ?";
            PreparedStatement ps2 = connection.prepareStatement(SQL2);
            ps2.setInt(1, boardid);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                storyid = rs2.getInt("storyid");
                SQL2 = "DELETE FROM task WHERE storyid = ?";
                ps2 = connection.prepareStatement(SQL2);
                ps2.setInt(1, storyid);
                ps2.executeUpdate();

                SQL = "DELETE FROM storyuser WHERE storyid=? ";
                ps = connection.prepareStatement(SQL);
                ps.setInt(1, storyid);
                ps.executeUpdate();
            }

            SQL = "DELETE FROM story WHERE boardid = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, boardid);
            ps.executeUpdate();

            SQL = "DELETE FROM board WHERE boardid = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, boardid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
