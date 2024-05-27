package com.example.eksamensprojektprojektmanager;

import com.example.eksamensprojektprojektmanager.model.Project;
import com.example.eksamensprojektprojektmanager.repository.AccountRepository;
import com.example.eksamensprojektprojektmanager.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:h2init.sql")
public class ProjectRepositoryH2Test {
    @Autowired
    ProjectRepository repository;

    @Test
    void createProjectTest() {

        Project newProject = new Project();
        newProject.setProjectName("Test Project");
        newProject.setUserId(1L);
        newProject.setStartDate(LocalDate.of(2022, 1, 1));
        newProject.setProjectDeadline(LocalDate.of(2022, 12, 31));
        newProject.setProjectStatus("Active");

        Project savedProject = repository.addProject(newProject);

        assertNotNull(savedProject);
        assertEquals("Test Project", savedProject.getProjectName());
    }

    @Test
    void editProjectTest() {

        Project existingProject = repository.getProjectById(1L);

        assertNotNull(existingProject);

        existingProject.setProjectName("Updated Project");
        existingProject.setStartDate(LocalDate.of(2023, 3, 22));
        existingProject.setProjectDeadline(LocalDate.of(2023, 6, 28));
        existingProject.setProjectStatus("Updated");

        Project updatedProject = repository.updateProject(existingProject);

        assertNotNull(updatedProject);
        assertEquals("Updated Project", updatedProject.getProjectName());
        assertEquals(LocalDate.of(2023, 3, 22), updatedProject.getStartDate());
        assertEquals(LocalDate.of(2023, 6, 28), updatedProject.getProjectDeadline());
        assertEquals("Updated", updatedProject.getProjectStatus());
    }

    @Test
    void deleteProjectTest() {

        Project existingProject = repository.getProjectById(1L);

        assertNotNull(existingProject);

        repository.deleteProjectById(existingProject.getProject_id());

        Project deletedProject = null;
        try {
            deletedProject = repository.getProjectById(existingProject.getProject_id());
        } catch (EmptyResultDataAccessException e) {
        }

        assertNull(deletedProject);
    }


}
