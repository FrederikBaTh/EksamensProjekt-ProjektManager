package com.example.eksamensprojektprojektmanager.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long project_id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "name",nullable = false, unique = false)
    public String projectName;

    @Column(name = "description",nullable = false)
    public String description;

    @Column(name = "startDate",nullable = false)
    @ColumnDefault("'1970-01-01'")
    public LocalDate startDate;

    @Column(name = "deadline",nullable = true)
    public LocalDate projectDeadline;

    //TODO ikke i datebasen endnu
    @Column(nullable = true)
    public String projectStatus;



    public Long getUser_id() {
        return user_id;
    }

    public void setUserId(Long user_id) {
        this.user_id = user_id;
    }
    public Project(String projectName,String description, LocalDate startDate, LocalDate projectDeadline ) {
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.projectDeadline = projectDeadline;
    }

    public Long getProject_id() {
        return project_id;
    }

    public void setProject_id(Long project_id) {
        this.project_id = project_id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getProjectDeadline() {
        return projectDeadline;
    }

    public void setProjectDeadline(LocalDate projectDeadline) {
        this.projectDeadline = projectDeadline;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Project() {
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + project_id +
                ", projectName='" + projectName + '\'' +
                ", startDate=" + startDate +
                ", projectDeadline=" + projectDeadline +
                ", projectStatus='" + projectStatus + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
