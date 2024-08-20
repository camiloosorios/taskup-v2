package com.bosorio.taskupv2.controllers;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.DTOs.UserDTO;
import com.bosorio.taskupv2.services.ProjectService;
import com.bosorio.taskupv2.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.bosorio.taskupv2.utils.HandlerExceptions.handleExceptions;

@RestController
@RequestMapping("/api/projects/{projectId}/team")
public class TeamMemberController {

    private final UserService userService;

    private final ProjectService projectService;

    @Autowired
    public TeamMemberController(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }

    @PostMapping("/find")
    public ResponseEntity<?> findMemberByEmail(@RequestBody UserDTO userDTO) {
        try {
            UserDTO user = userService.findUserByEmail(userDTO);

            return ResponseEntity.ok().body(user);
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> addMember(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        ProjectDTO projectDTO = (ProjectDTO) request.getAttribute("projectDTO");
        System.out.println(userDTO);
        System.out.println(projectDTO);
        try {
            projectService.addMember(userDTO.getId(), projectDTO);

            return ResponseEntity.ok().body("Member added successfully");
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeMember(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        ProjectDTO projectDTO = (ProjectDTO) request.getAttribute("projectDTO");
        try {
            projectService.removeMember(userDTO.getId(), projectDTO);

            return ResponseEntity.ok().body("Member deleted successfully");
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> getProjectMembers(HttpServletRequest request) {
        ProjectDTO projectDTO = (ProjectDTO) request.getAttribute("projectDTO");
        try {
            Set<UserDTO> members = projectService.getProjectMembers(projectDTO);

            return ResponseEntity.ok().body(members);
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }
}
