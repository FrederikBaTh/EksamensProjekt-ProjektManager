package com.example.eksamensprojektprojektmanager.repository;

import com.example.eksamensprojektprojektmanager.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TaskRepository() {
    }

    private Connection connection;

    public void addTask(Task task, Long projectId, Long subprojectId) {
        String insertQuery = "INSERT INTO tasks (project_id, subproject_id, name, description, date, deadline, status) VALUES (?, ?, ?, ?, ?, ?,?)";
        int rowsInserted = jdbcTemplate.update(insertQuery, projectId, subprojectId,
                task.getName(), task.getDescription(), task.getDate(), task.getDeadline(),task.getStatus());
    }

    public void addTaskJPA(Task task, Long projectId, Long subprojectId) {
        String insertTaskQuery = "INSERT INTO tasks (project_id, subproject_id, name, description, date, deadline, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement insertTaskStatement = connection.prepareStatement(insertTaskQuery)) {

            insertTaskStatement.setLong(1, projectId);
            insertTaskStatement.setLong(2, subprojectId);
            insertTaskStatement.setString(3, task.getName());
            insertTaskStatement.setString(4, task.getDescription());
            insertTaskStatement.setTimestamp(5, Timestamp.valueOf(task.getDate()));
            insertTaskStatement.setTimestamp(6, Timestamp.valueOf(task.getDeadline()));
            insertTaskStatement.setString(7, task.getStatus());

            insertTaskStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> findByProjectIdAndSubprojectId(Long projectId, Long subprojectId) {
        String query = "SELECT * FROM tasks WHERE project_id = ? AND subproject_id = ? ORDER BY status";
        return jdbcTemplate.query(query, new Object[]{projectId, subprojectId}, (resultSet, i) -> {
            Task task = new Task();
            task.setTask_id(resultSet.getLong("task_id"));
            task.setName(resultSet.getString("name"));
            task.setDescription(resultSet.getString("description"));
            task.setDate(resultSet.getTimestamp("date").toLocalDateTime());
            task.setDeadline(resultSet.getTimestamp("deadline").toLocalDateTime());
            task.setStatus(resultSet.getString("status"));
            return task;
        });
    }

    public List<Task> findByProjectIdAndSubProjectIdJPA(Long projectId, Long subprojectId) {
        String selectTasksQuery = "SELECT * FROM tasks WHERE project_id = ? AND subproject_id = ? ORDER BY status";

        List<Task> tasks = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement selectTasksStatement = connection.prepareStatement(selectTasksQuery)) {

            selectTasksStatement.setLong(1, projectId);
            selectTasksStatement.setLong(2, subprojectId);

            ResultSet resultSet = selectTasksStatement.executeQuery();

            while (resultSet.next()) {
                Task task = new Task();
                task.setTask_id(resultSet.getLong("task_id"));
                task.setName(resultSet.getString("name"));
                task.setDescription(resultSet.getString("description"));
                task.setDate(resultSet.getTimestamp("date").toLocalDateTime());
                task.setDeadline(resultSet.getTimestamp("deadline").toLocalDateTime());
                task.setStatus(resultSet.getString("status"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }


public Task findById(Long task_id) {
    String query = "SELECT * FROM tasks WHERE task_id = ?";
    return jdbcTemplate.queryForObject(query, new Object[]{task_id}, (resultSet, i) -> {
        Task task = new Task();
        task.setProjectId(resultSet.getLong("project_id"));
        task.setSubprojectId(resultSet.getLong("subproject_id"));
        task.setTask_id(resultSet.getLong("task_id"));
        task.setName(resultSet.getString("name"));
        task.setDescription(resultSet.getString("description"));
        task.setDate(resultSet.getTimestamp("date").toLocalDateTime());
        task.setDeadline(resultSet.getTimestamp("deadline").toLocalDateTime());
        task.setStatus(resultSet.getString("status")); // Set the status here
        return task;
    });
}

    public void deleteTaskById(Long task_id) {
        String query = "DELETE FROM tasks WHERE task_id = ?";
        jdbcTemplate.update(query, task_id);
    }

    public boolean deleteTaskByIdJPA(Long taskId) {
    String deleteTaskQuery = "DELETE FROM tasks WHERE task_id = ?";
    try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
         PreparedStatement deleteTaskStatement = connection.prepareStatement(deleteTaskQuery)) {

        deleteTaskStatement.setLong(1, taskId);

        int taskDeleted = deleteTaskStatement.executeUpdate();

        if (taskDeleted > 0) {
            return true;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
}

    public void deleteTasksBySubprojectId(Long subprojectId) {
        String query = "DELETE FROM tasks WHERE subproject_id = ?";
        jdbcTemplate.update(query, subprojectId);
    }

    public Task updateTask(Task task) {
        String updateQuery = "UPDATE tasks SET name = ?, description = ?, date = ?, deadline = ? WHERE task_id = ?";
        int rowsUpdated = jdbcTemplate.update(updateQuery, task.getName(), task.getDescription(), task.getDate(), task.getDeadline(), task.getTask_id());
        if (rowsUpdated > 0) {
            return task;
        }
        return null;
    }


    public void updateTaskStatus(Long task_id, String status) {
        String updateQuery = "UPDATE tasks SET status = ? WHERE task_id = ?";
        int rowsUpdated = jdbcTemplate.update(updateQuery, status, task_id);
        if (rowsUpdated > 0) {
            System.out.println("Task status was updated successfully!");
        }
    }


}