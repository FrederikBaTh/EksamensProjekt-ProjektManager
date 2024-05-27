/*package com.example.eksamensprojektprojektmanager;

import com.example.eksamensprojektprojektmanager.model.Task;
import com.example.eksamensprojektprojektmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
public class TaskRepositoryH2Test {
    @Autowired
    TaskRepository repository;

    @Test
    void createTaskTest() {
        // Create a new Task object
        Task newTask = new Task();
        newTask.setName("Test Task");
        newTask.setDescription("Test Description");
        newTask.setProjectId(1L);
        newTask.setSubprojectId(1L);
        newTask.setDate(LocalDateTime.of(2022, 1, 1,10,45));
        newTask.setDeadline(LocalDateTime.of(2022, 12, 31, 23, 59));
        newTask.setStatus("pending");

        // Save the new task to the repository
        repository.addTask(newTask, 1L, 1L);

        // Retrieve the saved task from the repository
        Task savedTask = repository.findById(4L);

        // Verify that the task was saved successfully
        assertNotNull(savedTask);
        assertEquals("Test Task", savedTask.getName());
    }

    @Test
    void editTaskTest() {
        // Retrieve an existing task from the repository
        Task existingTask = repository.findById(1L);

        // Verify that the task was retrieved successfully
        assertNotNull(existingTask);

        // Edit the task
        existingTask.setName("Updated Task");
        existingTask.setDescription("Updated description");
        existingTask.setDate(LocalDateTime.of(2023, 4, 7,6,12));
        existingTask.setDeadline(LocalDateTime.of(2024, 2, 2,16,50));
        existingTask.setStatus("completed");

        // Update the task in the repository
        Task updatedTask = repository.updateTask(existingTask);

        // Verify that the task was updated successfully
        assertNotNull(updatedTask);
        assertEquals("Updated Task", updatedTask.getName());
        assertEquals("Updated description",updatedTask.getDescription());
        assertEquals(LocalDateTime.of(2023, 4, 7,6,12), updatedTask.getDate());
        assertEquals(LocalDateTime.of(2024, 2, 2,16,50), updatedTask.getDeadline());
        assertEquals("completed", updatedTask.getStatus());
    }

    @Test
    void deleteTaskTest() {
        // Retrieve an existing task from the repository
        Task existingTask = repository.findById(1L);

        // Verify that the task was retrieved successfully
        assertNotNull(existingTask);

        // Delete the task
        repository.deleteTaskById(1L);

        // Try to retrieve the deleted task
        Task deletedTask = null;
        try {
            deletedTask = repository.findById(existingTask.getTask_id());
        } catch (EmptyResultDataAccessException e) {
            // This exception is expected because the task has been deleted
        }

        // Verify that the task was deleted successfully
        assertNull(deletedTask);
    }


} */