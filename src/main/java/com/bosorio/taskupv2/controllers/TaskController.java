package com.bosorio.taskupv2.controllers;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.DTOs.TaskDTO;
import com.bosorio.taskupv2.Exceptions.ForbbidenException;
import com.bosorio.taskupv2.Exceptions.NotFoundException;
import com.bosorio.taskupv2.services.TaskService;
import com.bosorio.taskupv2.utils.HandlerExceptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bosorio.taskupv2.utils.HandlerExceptions.handleExceptions;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDTO> getAllProjectTasks(HttpServletRequest request) {
        ProjectDTO projectDTO = (ProjectDTO) request.getAttribute("projectDTO");

        return taskService.getProjectTasks(projectDTO);
    }

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskDTO taskDTO, HttpServletRequest request) {
        try {
            ProjectDTO projectDTO = (ProjectDTO) request.getAttribute("projectDTO");
            taskService.createTask(taskDTO, projectDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Task created successfully");
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id, HttpServletRequest request) {
        ProjectDTO projectDTO = (ProjectDTO) request.getAttribute("projectDTO");
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(taskService.getTaskById(projectDTO, id));
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO,
                                        HttpServletRequest request) {
        ProjectDTO projectDTO = (ProjectDTO) request.getAttribute("projectDTO");
        try {
            taskService.updateTask(projectDTO, id, taskDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Task updated successfully");
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<?> handleEmptyBodyExceptions(HttpMessageNotReadableException ex) {
        return HandlerExceptions.handleEmptyBody();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return HandlerExceptions.handleValidations(ex);
    }

}
