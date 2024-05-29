package com.example.eksamensprojektprojektmanager.repository;

import com.example.eksamensprojektprojektmanager.model.Account;
import com.example.eksamensprojektprojektmanager.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.List;

@Repository
public class AccountRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ProjectInvitationRepository projectInvitationRepository;
    private final UserSubprojectAssignmentRepository userSubprojectAssignmentRepository;
    private final ProjectRepository projectRepository;


    @Value("${spring.datasource.url}")
    private String DATABASE_URL;
    @Value("${spring.datasource.username}")
    private String DATABASE_USERNAME;
    @Value("${spring.datasource.password}")
    private String DATABASE_PASSWORD;



    @Autowired
    public AccountRepository(JdbcTemplate jdbcTemplate, ProjectInvitationRepository projectInvitationRepository, UserSubprojectAssignmentRepository userSubprojectAssignmentRepository, ProjectRepository projectRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.projectInvitationRepository = projectInvitationRepository;
        this.userSubprojectAssignmentRepository = userSubprojectAssignmentRepository;
        this.projectRepository = projectRepository;
    }

    public boolean isValidUser(String username, String password) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
        return count > 0;
    }

    public String getUserIdByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, String.class, username);
    }

    public void save(Account account) {
        String sql = "INSERT INTO users (username, password, is_Admin, name, company, job_title, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, account.getUsername(), account.getPassword(), account.isAdmin(), account.getName(), account.getCompany(), account.getJobTitle(), account.getDescription());
    }

    public List<Account> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Account account = new Account();
            account.setUser_id(resultSet.getLong("user_id"));
            account.setUsername(resultSet.getString("username"));
            account.setPassword(resultSet.getString("password"));
            account.setAdmin(resultSet.getBoolean("is_admin"));
            account.setName(resultSet.getString("name"));
            account.setCompany(resultSet.getString("company"));
            account.setJobTitle(resultSet.getString("job_title"));
            account.setDescription(resultSet.getString("description"));
            return account;
        });
    }

    public Account getUserById(Long userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
            Account account = new Account();
            account.setUser_id(resultSet.getLong("user_id"));
            account.setUsername(resultSet.getString("username"));
            account.setPassword(resultSet.getString("password"));
            account.setAdmin(resultSet.getBoolean("is_admin"));
            account.setName(resultSet.getString("name"));
            account.setCompany(resultSet.getString("company"));
            account.setJobTitle(resultSet.getString("job_title"));
            account.setDescription(resultSet.getString("description"));
            return account;
        }, userId);
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count > 0;
    }

    public void updateAccount(Account account) {
        String sql = "UPDATE users SET name = ?, company = ?, job_title = ?, description = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, account.getName(), account.getCompany(), account.getJobTitle(), account.getDescription(), account.getUser_id());
    }

    public void deleteUser(Long userId) {

        projectInvitationRepository.deleteInvitationsForUser(userId);


        userSubprojectAssignmentRepository.deleteUserAssignmentsSubprojects(userId);


        List<Project> userProjects = projectRepository.getProjectsByUserId(userId);


        for (Project project : userProjects) {
            projectRepository.deleteProjectById(project.getProject_id());
        }


        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }




}