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

        String userIdString = (String) request.getSession().getAttribute("userId");
        if (userIdString != null) {
            Long userId = Long.parseLong(userIdString);


            List<ProjectInvitation> projectInvitation = projectInvitationService.getInvitesByUserId(userId);


            model.addAttribute("projectInvitation", projectInvitation);
        }


        return "invites";
    }


    @GetMapping("/invitedProjects")
    public String showInvitedProjects(HttpServletRequest request, Model model) {
        String userIdString = (String) request.getSession().getAttribute("userId");

        long userId = Long.parseLong(userIdString);


        List<Long> acceptedProjectIds = projectInvitationService.getAcceptedProjectIdsByUserId(userId);


        List<Project> invitedProjects = projectService.getProjectsByProjectIds(acceptedProjectIds);

        System.out.println("Retrieved invited projects: " + invitedProjects);

        model.addAttribute("invitedProjects", invitedProjects);

        return "invitedProjects";
    }
    @PostMapping("/acceptInvite")
    public String acceptInvite(@RequestParam("inviteId") Long inviteId) {

        projectInvitationService.acceptInvite(inviteId);
        projectInvitationService.deleteInvitationsForUser(inviteId);
        return "redirect:/seeProjects";
    }
   //bliver ikke brugt
    @PostMapping("/declineInvite")
    public String declineInvite(@RequestParam("inviteId") Long inviteId) {

        projectInvitationService.declineInvite(inviteId);
        return "redirect:/invites";
    }


    @PostMapping("/deleteInvitations")
    public String deleteInvitations(HttpServletRequest request) {
        String userIdString = (String) request.getSession().getAttribute("userId");

        long userId = Long.parseLong(userIdString);


        projectInvitationService.deleteInvitationsForUser(userId);

        return "redirect:/invites";
    }









}
