package com.bosorio.taskupv2.controllers;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.Exceptions.ForbbidenException;
import com.bosorio.taskupv2.Exceptions.NotFoundException;
import com.bosorio.taskupv2.services.ProjectService;
import com.bosorio.taskupv2.utils.HandlerExceptions;
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
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectDTO> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        try {
            projectService.createProject(projectDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Project created successfully");
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(projectService.getProjectById(id));
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectDTO projectDTO) {
        try {
            projectService.updateProject(id, projectDTO);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Project updated successfully");
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Project deleted successfully");
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
