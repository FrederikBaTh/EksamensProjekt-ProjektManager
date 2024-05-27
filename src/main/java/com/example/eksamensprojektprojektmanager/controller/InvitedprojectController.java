package com.example.eksamensprojektprojektmanager.controller;

import com.example.eksamensprojektprojektmanager.model.Project;
import com.example.eksamensprojektprojektmanager.model.ProjectInvitation;
import com.example.eksamensprojektprojektmanager.service.ProjectInvitationService;
import com.example.eksamensprojektprojektmanager.service.ProjectService;
import com.example.eksamensprojektprojektmanager.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class InvitedprojectController {



    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    @Autowired
    public ProjectService projectService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProjectInvitationService projectInvitationService;



    @GetMapping("/invites")
    public String showInvites(Model model, HttpServletRequest request) {
        // Retrieve the logged in user's ID from the session
        String userIdString = (String) request.getSession().getAttribute("userId");
        if (userIdString != null) {
            Long userId = Long.parseLong(userIdString);

            // Assuming you have a method in your service to get the invites for a user
            List<ProjectInvitation> projectInvitation = projectInvitationService.getInvitesByUserId(userId);

            // Add the invites to the model
            model.addAttribute("projectInvitation", projectInvitation);
        }

        // Return the name of the view that will display the invites
        return "invites";
    }


    @GetMapping("/invitedProjects")
    public String showInvitedProjects(HttpServletRequest request, Model model) {
        String userIdString = (String) request.getSession().getAttribute("userId");

        long userId = Long.parseLong(userIdString);

        // Retrieve project IDs for accepted invitations
        List<Long> acceptedProjectIds = projectInvitationService.getAcceptedProjectIdsByUserId(userId);

        // Retrieve invited projects based on accepted project IDs
        List<Project> invitedProjects = projectService.getProjectsByProjectIds(acceptedProjectIds);

        System.out.println("Retrieved invited projects: " + invitedProjects);

        model.addAttribute("invitedProjects", invitedProjects);

        return "invitedProjects"; // HTML page name for displaying invited projects
    }
    @PostMapping("/acceptInvite")
    public String acceptInvite(@RequestParam("inviteId") Long inviteId) {
        // Assuming you have a method in your service to accept an invite
        projectInvitationService.acceptInvite(inviteId);
        projectInvitationService.deleteInvitationsForUser(inviteId);
        return "redirect:/seeProjects"; // Redirect to projects page after accepting the invite
    }
   //bliver ikke brugt
    @PostMapping("/declineInvite")
    public String declineInvite(@RequestParam("inviteId") Long inviteId) {
        // Assuming you have a method in your service to decline an invite
        projectInvitationService.declineInvite(inviteId);
        return "redirect:/invites"; // Redirect to invites page after declining the invite
    }


    @PostMapping("/deleteInvitations")
    public String deleteInvitations(HttpServletRequest request) {
        String userIdString = (String) request.getSession().getAttribute("userId");

        long userId = Long.parseLong(userIdString);

        // Assuming you have a method in your service to delete invitations for a user
        projectInvitationService.deleteInvitationsForUser(userId);

        return "redirect:/invites"; // Redirect to invites page after deleting the invitations
    }









}
