package com.example.eksamensprojektprojektmanager.controller;

import com.example.eksamensprojektprojektmanager.model.Project;
import com.example.eksamensprojektprojektmanager.model.Task;
import com.example.eksamensprojektprojektmanager.service.ProjectInvitationService;
import com.example.eksamensprojektprojektmanager.service.ProjectService;
import com.example.eksamensprojektprojektmanager.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ProjectController {


    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    @Autowired
    public ProjectService projectService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProjectInvitationService projectInvitationService;


    @GetMapping("/seeProjects")
    public String showProjects(HttpServletRequest request, Model model) {
        String userIdString = (String) request.getSession().getAttribute("userId");
        if (userIdString == null) {
            return "redirect:/login";
        }

        Long userId = Long.parseLong(userIdString);

        List<Project> userProjects = projectService.getProjectsByUserId(userId);

        System.out.println("Retrieved projects: " + userProjects);

        model.addAttribute("projects", userProjects);

        return "seeProjects";
    }



    @GetMapping("/addProject")
    public String showProjectPage() {
        return "addProject";
    }


    @PostMapping("/addProject")
    public String addProject(@RequestParam("projectName") String projectName, @RequestParam("description") String description, @RequestParam("startDate") String startDate, @RequestParam("projectDeadline") String projectDeadline, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String userIdString = (String) request.getSession().getAttribute("userId");

        if (userIdString == null || userIdString.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "User ID is missing. Please log in again.");
            return "redirect:/seeProjects";
        }

        try {
            Project project = new Project();
            project.setProjectName(projectName);
            Long userId = Long.parseLong(userIdString);
            project.setDescription(description);
            project.setDescription(description);
            project.setUserId(userId);


            if (startDate != null && !startDate.isEmpty()) {
                project.setStartDate(LocalDate.parse(startDate));
            }
            if (projectDeadline != null && !projectDeadline.isEmpty()) {
                project.setProjectDeadline(LocalDate.parse(projectDeadline));
            }


            Project addedProject = projectService.addProject(project, userIdString);

            redirectAttributes.addFlashAttribute("successMessage", "Project added successfully with ID: " + addedProject.getProject_id());
            return "redirect:/seeProjects";

        } catch (IllegalArgumentException e) {
            logger.error("Error while adding project", e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/seeProjects";
        }
    }
    @GetMapping("/updateProject/{id}")
    public String showUpdateForm(@PathVariable("id") Long projectId, Model model) {
        Project project = projectService.getProjectById(projectId);

        model.addAttribute("project", project);

        return "addProject";
    }




    @PostMapping("/updateProject/{projectId}")
    public String updateProject(@PathVariable Long projectId,
                                @RequestParam("projectName") String projectName,
                                @RequestParam("startDate") String startDate,
                                @RequestParam(value = "projectDeadline", required = false) String projectDeadline,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        String userIdString = (String) request.getSession().getAttribute("userId");

        if (userIdString == null || userIdString.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "User ID is missing. Please log in again.");
            return "redirect:/seeProjects";
        }

        try {
            Project project = new Project();
            project.setProject_id(projectId);
            project.setProjectName(projectName);
            Long userId = Long.parseLong(userIdString);
            project.setUserId(userId);

            if (startDate != null && !startDate.isEmpty()) {
                project.setStartDate(LocalDate.parse(startDate));
            }
            if (projectDeadline != null && !projectDeadline.isEmpty()) {
                project.setProjectDeadline(LocalDate.parse(projectDeadline));
            }

            Project updatedProject = projectService.updateProject(project, userIdString);

            redirectAttributes.addFlashAttribute("successMessage", "Project updated successfully with ID: " + updatedProject.getProject_id());
            return "redirect:/seeProjects";

        } catch (IllegalArgumentException e) {
            logger.error("Error while updating project", e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/seeProjects";
        }
    }



    @PostMapping("/deleteProject")
    public String deleteProject(@RequestParam("id") String projectIdStr, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String userIdString = (String) request.getSession().getAttribute("userId");

        if (userIdString == null || userIdString.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "User ID is missing. Please log in again.");
            return "redirect:/login";
        }

        try {
            Long projectId = Long.parseLong(projectIdStr);
            Long subprojectId = Long.parseLong(projectIdStr);

            List<Task> projectTasks = taskService.getTasksByProjectIdAndSubprojectId(projectId,subprojectId);
            if (!projectTasks.isEmpty()) {
                for (Task task : projectTasks) {
                    taskService.deleteTaskById(task.getTask_id());
                }
            }

            projectService.deleteProjectById(projectId);
            redirectAttributes.addFlashAttribute("successMessage", "Project and its tasks deleted successfully.");
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid project ID.");
        } catch (IllegalArgumentException e) {
            logger.error("Error while deleting project", e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/seeProjects";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/login";
    }

    //-------------------------------------------invited---------------------------------------------------------------




    @PostMapping("/inviteUserToProject")
    public String inviteUserToProject(@RequestParam("projectId") long projectId,
                                      @RequestParam("senderUserId") long senderUserId,
                                      @RequestParam("receiverUserId") long receiverUserId,
                                      RedirectAttributes redirectAttributes) {
        try {
            projectInvitationService.inviteUserToProject(projectId, senderUserId, receiverUserId);
            redirectAttributes.addFlashAttribute("successMessage", "User invited to project successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error while inviting user to project.");
        }
        return "redirect:/seeProjects";
    }































}