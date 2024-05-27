/*package com.example.eksamensprojektprojektmanager.test;


import com.example.eksamensprojektprojektmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
 class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    void testUpdateTaskStatus() {
        // Arrange
        Long taskId = Long.valueOf("1"); // replace with actual task id
        String newStatus = "completed"; // replace with actual status

        // Act
        taskRepository.updateTaskStatus(taskId, newStatus);

        // Assert
        String sql = "SELECT status FROM tasks WHERE task_id = ?";
        String actualStatus = jdbcTemplate.queryForObject(sql, new Object[]{taskId}, String.class);
        assertEquals(newStatus, actualStatus);
    }


}
*/