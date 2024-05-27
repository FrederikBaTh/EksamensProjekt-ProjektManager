package com.example.eksamensprojektprojektmanager;

import com.example.eksamensprojektprojektmanager.model.Subproject;
import com.example.eksamensprojektprojektmanager.repository.SubprojectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:h2init.sql")
public class SubprojectRepositoryH2Test {
    @Autowired
    SubprojectRepository repository;

    @Test
    void createSubprojectTest() {
        Subproject newSubproject = new Subproject();
        newSubproject.setSubprojectname("Test Subproject");
        newSubproject.setDescription("Test Description");
        newSubproject.setProject_id(1L);
        newSubproject.setStartDate(LocalDate.of(2022, 1, 1));
        newSubproject.setDeadline(LocalDate.of(2022, 12, 31));

        repository.addSubproject(newSubproject, 1L);

        Subproject savedSubproject = repository.findById(4L);

        assertNotNull(savedSubproject);
        assertEquals("Test Subproject", savedSubproject.getSubprojectname());
    }

    @Test
    void editSubprojectTest() {
        Subproject existingSubproject = repository.findById(1L);

        assertNotNull(existingSubproject);

        existingSubproject.setSubprojectname("Updated Subproject");
        existingSubproject.setDescription("Updated description");
        existingSubproject.setStartDate(LocalDate.of(2023, 12, 31));
        existingSubproject.setDeadline(LocalDate.of(2024, 1, 12));


        Subproject updatedSubproject = repository.updateSubproject(existingSubproject);

        assertNotNull(updatedSubproject);
        assertEquals("Updated Subproject", updatedSubproject.getSubprojectname());
        assertEquals("Updated description",updatedSubproject.getDescription());
        assertEquals(LocalDate.of(2023, 12, 31), updatedSubproject.getStartDate());
        assertEquals(LocalDate.of(2024, 1, 12), updatedSubproject.getDeadline());

    }

    @Test
    void deleteSubprojectTest() {
        Subproject existingSubproject = repository.findById(1L);

        assertNotNull(existingSubproject);

        repository.deleteSubprojectsByProjectId(1L);

        Subproject deletedSubproject = null;
        try {
            deletedSubproject = repository.findById(existingSubproject.getSubproject_id());
        } catch (EmptyResultDataAccessException e) {
        }

        assertNull(deletedSubproject);
    }


}