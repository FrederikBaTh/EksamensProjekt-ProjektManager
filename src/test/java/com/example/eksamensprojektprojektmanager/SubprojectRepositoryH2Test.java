/*package com.example.eksamensprojektprojektmanager;

import com.example.eksamensprojektprojektmanager.model.Subproject;
import com.example.eksamensprojektprojektmanager.repository.SubprojectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
public class SubprojectRepositoryH2Test {
    @Autowired
    SubprojectRepository repository;

    @Test
    void createSubprojectTest() {
        // Create a new Subproject object
        Subproject newSubproject = new Subproject();
        newSubproject.setSubprojectname("Test Subproject");
        newSubproject.setDescription("Test Description");
        newSubproject.setProject_id(1L);
        newSubproject.setStartDate(LocalDate.of(2022, 1, 1));
        newSubproject.setDeadline(LocalDate.of(2022, 12, 31));

        // Save the new subproject to the repository
        repository.addSubproject(newSubproject, 1L);

        // Retrieve the saved subproject from the repository
        Subproject savedSubproject = repository.findById(4L);

        // Verify that the subproject was saved successfully
        assertNotNull(savedSubproject);
        assertEquals("Test Subproject", savedSubproject.getSubprojectname());
    }

    @Test
    void editSubprojectTest() {
        // Retrieve an existing subproject from the repository
        Subproject existingSubproject = repository.findById(1L);

        // Verify that the subproject was retrieved successfully
        assertNotNull(existingSubproject);

        // Edit the subproject
        existingSubproject.setSubprojectname("Updated Subproject");
        existingSubproject.setDescription("Updated description");
        existingSubproject.setStartDate(LocalDate.of(2023, 12, 31));
        existingSubproject.setDeadline(LocalDate.of(2024, 1, 12));


        // Update the subproject in the repository
        Subproject updatedSubproject = repository.updateSubproject(existingSubproject);

        // Verify that the subproject was updated successfully
        assertNotNull(updatedSubproject);
        assertEquals("Updated Subproject", updatedSubproject.getSubprojectname());
        assertEquals("Updated description",updatedSubproject.getDescription());
        assertEquals(LocalDate.of(2023, 12, 31), updatedSubproject.getStartDate());
        assertEquals(LocalDate.of(2024, 1, 12), updatedSubproject.getDeadline());

    }

    @Test
    void deleteSubprojectTest() {
        // Retrieve an existing subproject from the repository
        Subproject existingSubproject = repository.findById(1L);

        // Verify that the subproject was retrieved successfully
        assertNotNull(existingSubproject);

        // Delete the subproject
        repository.deleteSubprojectsByProjectId(1L);

        // Try to retrieve the deleted subproject
        Subproject deletedSubproject = null;
        try {
            deletedSubproject = repository.findById(existingSubproject.getSubproject_id());
        } catch (EmptyResultDataAccessException e) {
            // This exception is expected because the subproject has been deleted
        }

        // Verify that the subproject was deleted successfully
        assertNull(deletedSubproject);
    }


} */