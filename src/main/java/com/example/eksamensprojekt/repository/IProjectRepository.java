package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.dto.ProjectDTOForm;
import com.example.eksamensprojekt.model.Project;

import java.sql.Date;
import java.util.List;

public interface IProjectRepository {
    List<Project> getProjects(int userid);
    Project getSpecificProject(int projectid);
    void addProject(int userid, ProjectDTOForm project);
    List<String> getUserNamesByProjectId(int projectid);
    void deleteProject(int projectid);
    void updateProjectName(int projectid, String projectname);
    void updateProjectDeadline(int projectid, Date projectdeadline);
    // OBS! de 3 nederste metoder bliver benyttet som hjælpe metoder i denne klasse
    void addUserToProject(int userid, int projectid);
    void addBoard(int projectid, String boardname);
    void deleteBoard(int boardid);
}
