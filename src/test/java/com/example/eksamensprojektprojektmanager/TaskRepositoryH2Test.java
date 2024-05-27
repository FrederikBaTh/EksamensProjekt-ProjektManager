package com.example.eksamensprojektprojektmanager;

import com.example.eksamensprojektprojektmanager.model.Task;
import com.example.eksamensprojektprojektmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:h2init.sql")
public class TaskRepositoryH2Test {
    @Autowired
    TaskRepository repository;

    @Test
    void createTaskTest() {

        Task newTask = new Task();
        newTask.setName("Test Task");
        newTask.setDescription("Test Description");
        newTask.setProjectId(1L);
        newTask.setSubprojectId(1L);
        newTask.setDate(LocalDateTime.of(2022, 1, 1,10,45));
        newTask.setDeadline(LocalDateTime.of(2022, 12, 31, 23, 59));
        newTask.setStatus("pending");

        repository.addTask(newTask, 1L, 1L);

        Task savedTask = repository.findById(4L);

        assertNotNull(savedTask);
        assertEquals("Test Task", savedTask.getName());
    }

    @Test
    void editTaskTest() {
        Task existingTask = repository.findById(1L);

        assertNotNull(existingTask);

        existingTask.setName("Updated Task");
        existingTask.setDescription("Updated description");
        existingTask.setDate(LocalDateTime.of(2023, 4, 7,6,12));
        existingTask.setDeadline(LocalDateTime.of(2024, 2, 2,16,50));
        existingTask.setStatus("completed");

        Task updatedTask = repository.updateTask(existingTask);

        assertNotNull(updatedTask);
        assertEquals("Updated Task", updatedTask.getName());
        assertEquals("Updated description",updatedTask.getDescription());
        assertEquals(LocalDateTime.of(2023, 4, 7,6,12), updatedTask.getDate());
        assertEquals(LocalDateTime.of(2024, 2, 2,16,50), updatedTask.getDeadline());
        assertEquals("completed", updatedTask.getStatus());
    }

    @Test
    void deleteTaskTest() {

        Task existingTask = repository.findById(1L);

        assertNotNull(existingTask);

        repository.deleteTaskById(1L);

        Task deletedTask = null;
        try {
            deletedTask = repository.findById(existingTask.getTask_id());
        } catch (EmptyResultDataAccessException e) {
        }

        assertNull(deletedTask);
    }


}