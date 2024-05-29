package com.example.eksamensprojektprojektmanager.controller;

import com.example.eksamensprojektprojektmanager.model.Account;
import com.example.eksamensprojektprojektmanager.model.Subproject;
import com.example.eksamensprojektprojektmanager.service.AccountService;
import com.example.eksamensprojektprojektmanager.service.ProjectInvitationService;
import com.example.eksamensprojektprojektmanager.service.SubprojectService;
import com.example.eksamensprojektprojektmanager.service.UserSubprojectAssignmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SubprojectController {

    private final SubprojectService subprojectService;

    private static final Logger logger = LoggerFactory.getLogger(SubprojectController.class);
    private final AccountService accountService;
    private final ProjectInvitationService projectInvitationService;
    private final UserSubprojectAssignmentService userSubprojectAssignmentService;


    @Autowired
    public SubprojectController(SubprojectService subprojectService, AccountService accountService, ProjectInvitationService projectInvitationService, UserSubprojectAssignmentService userSubprojectAssignmentService) {
        this.subprojectService = subprojectService;
        this.accountService = accountService;
        this.projectInvitationService = projectInvitationService;
        this.userSubprojectAssignmentService = userSubprojectAssignmentService;
    }

    @GetMapping("/subprojects/{projectId}")
    public String showSubprojects(@PathVariable Long projectId, Model model) {
        List<Subproject> subprojects = subprojectService.getSubprojectsByProjectId(projectId);
        List<Long> userIds = projectInvitationService.getAcceptedUserIdsByProjectId(projectId);
        List<Account> users = accountService.getUsersByIdsList(userIds); // Fetch the users who have accepted the invite


        Map<Long, List<Account>> subprojectAssignedUsers = new HashMap<>();


        for (Subproject subproject : subprojects) {
            List<Account> assignedUsers = subprojectService.getAssignedUsers(subproject.getSubproject_id());
            subprojectAssignedUsers.put(subproject.getSubproject_id(), assignedUsers);
        }

        model.addAttribute("subprojects", subprojects);
        model.addAttribute("projectId", projectId);
        model.addAttribute("users", users);
        model.addAttribute("subprojectAssignedUsers", subprojectAssignedUsers);
        return "seeSubprojects";
    }

    @GetMapping("/addSubproject/{projectId}")
    public String showAddSubprojectPage(@PathVariable Long projectId, Model model) {
        model.addAttribute("projectId", projectId);
        return "addSubproject";
    }

    @PostMapping("/addSubproject/{projectId}")
    public String addSubproject(@PathVariable Long projectId,
                                @ModelAttribute Subproject subproject,
                                RedirectAttributes redirectAttributes) {

        subprojectService.addSubproject(subproject, projectId);
        redirectAttributes.addFlashAttribute("successMessage", "Subproject added successfully.");
        return "redirect:/subprojects/" + projectId;
    }

    @PostMapping("/deleteSubproject/{projectId}/{subprojectId}")
    public String deleteSubproject(@PathVariable Long projectId, @PathVariable Long subprojectId, RedirectAttributes redirectAttributes) {
        Subproject subproject = subprojectService.getSubprojectById(subprojectId);

        subprojectService.deleteSubprojectById(subprojectId);
        redirectAttributes.addFlashAttribute("successMessage", "Subproject deleted successfully.");
        return "redirect:/subprojects/" + projectId;
    }

    @GetMapping("/updateSubproject/{id}")
    public String showUpdateForm(@PathVariable("id") Long subprojectId, Model model) {
        logger.info("Subproject ID: " + subprojectId);
        Subproject subproject = subprojectService.getSubprojectById(subprojectId);

        model.addAttribute("subproject", subproject);

        return "addSubproject";
    }

    @PostMapping("/updateSubproject/{id}")
    public String updateSubproject(@PathVariable("id") Long subprojectId,
                                   @ModelAttribute Subproject updatedSubproject,
                                   RedirectAttributes redirectAttributes) {
        Subproject existingSubproject = subprojectService.getSubprojectById(subprojectId);

        updatedSubproject.setSubproject_id(subprojectId);
        updatedSubproject.setProject_id(existingSubproject.getProject_id());
        Subproject updatedSubprojectInDb = subprojectService.updateSubproject(updatedSubproject);
        redirectAttributes.addFlashAttribute("successMessage", "Subproject updated successfully with ID: " + updatedSubprojectInDb.getSubproject_id());
        return "redirect:/subprojects/" + updatedSubproject.getProject_id();
    }

    @PostMapping("/assignUserToSubproject")
    public String assignUserToSubproject(@RequestParam Long userId, @RequestParam Long subprojectId, RedirectAttributes redirectAttributes) {
        // Fetch the user and subproject from the database
        Account user = accountService.getUserById(userId);
        Subproject subproject = subprojectService.getSubprojectById(subprojectId);



        userSubprojectAssignmentService.assignUserToSubproject(user.getUser_id(), subproject.getSubproject_id());

        redirectAttributes.addFlashAttribute("successMessage", "User assigned to subproject successfully.");
        return "redirect:/subprojects/" + subprojectId;
    }
}