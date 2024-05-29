package com.example.eksamensprojektprojektmanager.controller;

import com.example.eksamensprojektprojektmanager.model.Experience;
import com.example.eksamensprojektprojektmanager.model.Project;
import com.example.eksamensprojektprojektmanager.service.ProjectInvitationService;
import com.example.eksamensprojektprojektmanager.service.ProjectService;
import org.springframework.ui.Model;
import com.example.eksamensprojektprojektmanager.model.Account;
import com.example.eksamensprojektprojektmanager.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    @Autowired
    private AccountService accountService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectInvitationService projectInvitationService;


    @GetMapping("/")
    public RedirectView redirectToLogin() {
        return new RedirectView("/login");
    }


    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }


    @PostMapping("/register")
    public String registerAccount(Account account, @RequestParam(value = "isAdmin", required = false) Boolean isAdmin) {
        if (isAdmin == null) {
            isAdmin = false;
        }
        account.setAdmin(isAdmin);

        if (account.getName() == null) {
            account.setName("");
        }
        if (account.getCompany() == null) {
            account.setCompany("");
        }
        if (account.getJobTitle() == null) {
            account.setJobTitle("");
        }
        if (account.getDescription() == null) {
            account.setDescription("");
        }

        accountService.saveAccount(account);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, @RequestParam("username") String username, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        if (accountService.isValidUser(username, password)) {
            String userId = accountService.getUserIdByUsername(username);
            request.getSession().setAttribute("userId", userId);
            return "redirect:/seeProjects";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid login");
            return "redirect:/login";
        }
    }

    @GetMapping("/usersList")
    public String showUserList(Model model, HttpServletRequest request) {
        List<Account> userList = accountService.getAllUsers();
        Map<Long, Experience> userTopExperiences = new HashMap<>();

        for (Account user : userList) {
            List<Experience> experiences = accountService.getExperiencesByUserId(user.getUser_id());
            if (!experiences.isEmpty()) {
                Experience topExperience = experiences.stream()
                        .max(Comparator.comparingInt(Experience::getYearsOfExperience))
                        .orElse(null);
                userTopExperiences.put(user.getUser_id(), topExperience);
            }
        }

        model.addAttribute("users", userList);
        model.addAttribute("userTopExperiences", userTopExperiences);

        String loggedInUserId = (String) request.getSession().getAttribute("userId");
        model.addAttribute("loggedInUserId", loggedInUserId);

        String projectIdString = (String) request.getSession().getAttribute("projectId");
        if (projectIdString != null) {
            Long projectId = Long.parseLong(projectIdString);
            model.addAttribute("projectId", projectId);
        }


        String userIdString = (String) request.getSession().getAttribute("userId");
        Long userId = Long.parseLong(userIdString);
        List<Project> userProjects = projectService.getProjectsByUserId(userId);
        model.addAttribute("projects", userProjects);

        return "userList";
    }

    @GetMapping("/myAccount")
    public String getMyAccount(HttpServletRequest request, Model model) {
        String userIdStr = (String) request.getSession().getAttribute("userId");
        if (userIdStr != null) {
            Long userId = Long.valueOf(userIdStr);
            Account user = accountService.getUserById(userId);
            List<Experience> experiences = accountService.getExperiencesByUserId(userId);
            model.addAttribute("user", user);
            model.addAttribute("experiences", experiences);
            return "myAccount";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/editAccount")
    public String showEditAccount(Model model, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");
        if (userId != null) {
            Account user = accountService.getUserById(Long.valueOf(userId));
            if (user != null) {
                model.addAttribute("user", user);
                return "editAccount";
            }
        }
        return "redirect:/login";
    }

    @PostMapping("/editUser")
    public String editUser(@RequestParam("userId") Long userId,
                           @RequestParam("name") String name,
                           @RequestParam("company") String company,
                           @RequestParam("jobTitle") String jobTitle,
                           @RequestParam("description") String description,
                           RedirectAttributes redirectAttributes) {

        Account user = accountService.getUserById(userId);
        if (user != null) {
            user.setName(name);
            user.setCompany(company);
            user.setJobTitle(jobTitle);
            user.setDescription(description);

            accountService.updateAccount(user);

            return "redirect:/myAccount";
        } else {
            return "redirect:/login";
        }
    }





    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam Long userId) {
        accountService.deleteUser(userId);
        return "redirect:/login";
    }

    @PostMapping("/addExperience")
    public String addExperience(HttpServletRequest request,
                                @RequestParam("skill") String skill,
                                @RequestParam("yearsOfExperience") int yearsOfExperience,
                                RedirectAttributes redirectAttributes) {
        String userIdStr = (String) request.getSession().getAttribute("userId");
        if (userIdStr != null) {
            Long userId = Long.valueOf(userIdStr);
            accountService.addExperienceToUser(userId, skill, yearsOfExperience);
            redirectAttributes.addFlashAttribute("success", "Experience added successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "User not logged in");
        }
        return "redirect:/myAccount";
    }

    @PostMapping("/deleteExperience")
    public String deleteExperience(@RequestParam("experienceId") Long experienceId,
                                   RedirectAttributes redirectAttributes) {
        accountService.deleteExperience(experienceId);
        redirectAttributes.addFlashAttribute("success", "Experience deleted successfully");
        return "redirect:/myAccount";
    }

    @GetMapping("/userDetails")
    public String showUserDetails(Model model, @RequestParam("userId") Long userId, HttpServletRequest request) {
        Account user = accountService.getUserById(userId);
        List<Experience> experiences = accountService.getExperiencesByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("experiences", experiences);

        String loggedInUserId = (String) request.getSession().getAttribute("userId");
        model.addAttribute("loggedInUserId", loggedInUserId);

        return "userDetails";
    }
}






