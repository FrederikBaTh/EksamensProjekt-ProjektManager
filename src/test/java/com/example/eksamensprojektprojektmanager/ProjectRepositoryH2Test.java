/*package com.example.eksamensprojektprojektmanager;

import com.example.eksamensprojektprojektmanager.model.Project;
import com.example.eksamensprojektprojektmanager.repository.AccountRepository;
import com.example.eksamensprojektprojektmanager.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
public class ProjectRepositoryH2Test {
    @Autowired
    ProjectRepository repository;

    @Test
    void createProjectTest() {
        // Create a new Project object
        Project newProject = new Project();
        newProject.setProjectName("Test Project");
        newProject.setUserId(1L);
        newProject.setStartDate(LocalDate.of(2022, 1, 1));
        newProject.setProjectDeadline(LocalDate.of(2022, 12, 31));
        newProject.setProjectStatus("Active");

        // Save the new project to the repository
        Project savedProject = repository.addProject(newProject);

        // Verify that the project was saved successfully
        assertNotNull(savedProject);
        assertEquals("Test Project", savedProject.getProjectName());
    }

    @Test
    void editProjectTest() {
        // Retrieve an existing project from the repository
        Project existingProject = repository.getProjectById(1L);

        // Verify that the project was retrieved successfully
        assertNotNull(existingProject);

        // Edit the project
        existingProject.setProjectName("Updated Project");
        existingProject.setStartDate(LocalDate.of(2023, 3, 22));
        existingProject.setProjectDeadline(LocalDate.of(2023, 6, 28));
        existingProject.setProjectStatus("Updated");

        // Update the project in the repository
        Project updatedProject = repository.updateProject(existingProject);

        // Verify that the project was updated successfully
        assertNotNull(updatedProject);
        assertEquals("Updated Project", updatedProject.getProjectName());
        assertEquals(LocalDate.of(2023, 3, 22), updatedProject.getStartDate());
        assertEquals(LocalDate.of(2023, 6, 28), updatedProject.getProjectDeadline());
        assertEquals("Updated", updatedProject.getProjectStatus());
    }

    @Test
    void deleteProjectTest() {
        // Retrieve an existing project from the repository
        Project existingProject = repository.getProjectById(1L);

        // Verify that the project was retrieved successfully
        assertNotNull(existingProject);

        // Delete the project
        repository.deleteProjectById(existingProject.getProject_id());

        // Try to retrieve the deleted project
        Project deletedProject = null;
        try {
            deletedProject = repository.getProjectById(existingProject.getProject_id());
        } catch (EmptyResultDataAccessException e) {
            // This exception is expected because the project has been deleted
        }

        // Verify that the project was deleted successfully
        assertNull(deletedProject);
    }


}
*/