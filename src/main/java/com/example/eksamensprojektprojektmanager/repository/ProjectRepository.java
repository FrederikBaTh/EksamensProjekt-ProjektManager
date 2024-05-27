package com.example.eksamensprojektprojektmanager.repository;

import com.example.eksamensprojektprojektmanager.model.InvitationStatus;
import com.example.eksamensprojektprojektmanager.model.Project;
import com.example.eksamensprojektprojektmanager.model.ProjectInvitation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class ProjectRepository {



    @Value("${spring.datasource.url}")
    private String db_url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Autowired
    public TaskRepository taskRepository;

    @Autowired
    public SubprojectRepository subprojectRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ProjectRepository() {
    }

    public Project addProject(Project project) {
    String insertQuery = "INSERT INTO projects (name, user_id, description, startDate, deadline) VALUES (?, ?, ?, ?, ?)";
    int rowsInserted = jdbcTemplate.update(insertQuery, project.getProjectName(),
            project.getUser_id(), project.getDescription(),
            java.sql.Date.valueOf(project.getStartDate()),
            java.sql.Date.valueOf(project.getProjectDeadline()));
    if (rowsInserted > 0) {
        System.out.println("A new project was added successfully!");
        return project;
    }
    return null;
}


    public boolean existsByNameAndUserId(String name, Long userId) {
        String query = "SELECT COUNT(*) FROM projects WHERE name = ? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, name, userId);
        return count != null && count > 0;
    }


    public List<Project> getProjectsByUserId(Long userId) {
        String query = "SELECT * FROM projects WHERE user_id = ?";
        return jdbcTemplate.query(query, (resultSet, rowNum) -> {
            Project project = new Project();
            project.setProject_id(resultSet.getLong("project_id"));
            project.setProjectName(resultSet.getString("name"));
            project.setStartDate(resultSet.getDate("startDate").toLocalDate());
            project.setProjectDeadline(resultSet.getDate("deadline").toLocalDate());
            return project;
        }, userId);
    }
    public Project updateProject(Project project) {
        String updateQuery = "UPDATE projects SET name = ?, description= ?, startDate = ?, deadline = ? WHERE project_id = ?";
        int rowsUpdated = jdbcTemplate.update(updateQuery, project.getProjectName(),
                project.getDescription(),
                java.sql.Date.valueOf(project.getStartDate()),
                java.sql.Date.valueOf(project.getProjectDeadline()),
                project.getProject_id());
        if (rowsUpdated > 0) {
            System.out.println("Project was updated successfully!");
            return project;
        }
        return null;
    }

    public Project getProjectById(Long projectId) {
        String query = "SELECT * FROM projects WHERE project_id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{projectId}, (resultSet, rowNum) -> {
            Project project = new Project();
            project.setProject_id(resultSet.getLong("project_id"));
            project.setProjectName(resultSet.getString("name"));
            project.setStartDate(resultSet.getDate("startDate").toLocalDate());
            project.setProjectDeadline(resultSet.getDate("deadline").toLocalDate());
            return project;
        });
    }

    public List<Project> getProjectsByProjectIds(List<Long> projectIds) {
        List<Project> projects = new ArrayList<>();
        for (Long projectId : projectIds) {
            Project project = getProjectById(projectId);
            if (project != null) {
                projects.add(project);
            }
        }
        return projects;
    }



    public boolean deleteProjectById(Long projectId) {
        try {
            taskRepository.deleteTaskById(projectId);
            subprojectRepository.deleteSubprojectsByProjectId(projectId);

            String query = "DELETE FROM projects WHERE project_id = ?";
            int rowsDeleted = jdbcTemplate.update(query, projectId);
            if (rowsDeleted > 0) {
                return true;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return false;
    }






}