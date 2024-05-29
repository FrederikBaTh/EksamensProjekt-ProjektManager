package com.example.eksamensprojektprojektmanager.controller;

import com.example.eksamensprojektprojektmanager.model.Task;
import com.example.eksamensprojektprojektmanager.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class TaskController {

    private final TaskService taskService;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks/{projectId}/{subprojectId}")
    public String showTasks(@PathVariable(required = false) Long projectId,
                            @PathVariable(required = false) Long subprojectId,
                            Model model) {


        logger.info("Entering showTasks method with projectId: {} and subprojectId: {}", projectId, subprojectId);
        List<Task> tasks = taskService.getTasksByProjectIdAndSubprojectId(projectId, subprojectId);
        model.addAttribute("tasks", tasks);
        model.addAttribute("projectId", projectId);
        model.addAttribute("subprojectId", subprojectId);
        return "seeTask";
    }


    @GetMapping("/addTask/{projectId}/{subprojectId}")
    public String showAddTaskPage(@PathVariable Long projectId,@PathVariable Long subprojectId, Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("subprojectId", subprojectId);


        return "addTask";
    }

    @PostMapping("/addTask/{projectId}/{subprojectId}")
    public String addTask(@PathVariable Long projectId,
                          @PathVariable Long subprojectId,
                          @RequestParam("taskName") String taskName,
                          @RequestParam("taskDescription") String taskDescription,
                          @RequestParam("taskDate")
                          @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime taskDateTime,
                          @RequestParam("taskDeadline")
                          @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime taskDeadlineDateTime,
                          RedirectAttributes redirectAttributes) {
        Task task = new Task(taskName, taskDescription, taskDateTime, taskDeadlineDateTime, "backlog");
        task.setProjectId(projectId);
        task.setSubprojectId(subprojectId);
        task.setStatus("backlog");
        taskService.addTask(task, projectId, subprojectId);
        redirectAttributes.addFlashAttribute("successMessage", "Task added successfully.");
        return "redirect:/tasks/" + projectId + "/" + subprojectId;
    }

    @PostMapping("/deleteTask/{projectId}/{taskId}/{subprojectId}")
    public String deleteTask(@PathVariable Long projectId, @PathVariable Long taskId, @PathVariable Long subprojectId, RedirectAttributes redirectAttributes) {
        Task task = taskService.getTaskById(taskId);

        taskService.deleteTaskById(taskId);
        redirectAttributes.addFlashAttribute("successMessage", "Task deleted successfully.");
        return "redirect:/tasks/" + projectId + "/" + subprojectId;
    }

    @GetMapping("/updateTask/{id}")
    public String showUpdateForm(@PathVariable("id") Long taskId, Model model) {
        logger.info("Task ID: " + taskId);

        logger.info("Task ID: " + taskId);
        Task task = taskService.getTaskById(taskId);
        logger.info("Retrieved task: " + task);
        model.addAttribute("task", task);
        model.addAttribute("projectId", task.getProjectId());
        model.addAttribute("subprojectId", task.getSubprojectId());
        return "addTask";
    }


    @PostMapping("/updateTask/{id}")
    public String updateTask(@PathVariable("id") Long taskId,
                             @ModelAttribute Task updatedTask,
                             RedirectAttributes redirectAttributes) {
        Task existingTask = taskService.getTaskById(taskId);


        Task task = taskService.getTaskById(taskId);



        updatedTask.setTask_id(taskId);
        existingTask.setName(updatedTask.getName());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setDate(updatedTask.getDate());
        existingTask.setDeadline(updatedTask.getDeadline());

        existingTask.setProjectId(updatedTask.getProjectId());
        existingTask.setSubprojectId(updatedTask.getSubprojectId());

        Task updatedTaskInDb = taskService.updateTask(existingTask);

        redirectAttributes.addFlashAttribute("successMessage", "Task updated successfully with ID: " + updatedTaskInDb.getTask_id());
        return "redirect:/tasks/" + updatedTask.getProjectId() + "/" + updatedTask.getSubprojectId();
    }




    @PostMapping("/updateTaskStatus/{id}")
public String updateTaskStatus(@PathVariable("id") Long taskId,
                               @RequestParam("status") String status,
                               RedirectAttributes redirectAttributes) {
    Task task = taskService.getTaskById(taskId);

    task.setStatus(status);
    taskService.updateTaskStatus(taskId, status);
    redirectAttributes.addFlashAttribute("successMessage", "Task status updated successfully.");
    return "redirect:/tasks/" + task.getProjectId() + "/" + task.getSubprojectId();
}



}