package com.bosorio.taskupv2.controllers;

import com.bosorio.taskupv2.DTOs.NoteDTO;
import com.bosorio.taskupv2.DTOs.TaskDTO;
import com.bosorio.taskupv2.services.NoteService;
import com.bosorio.taskupv2.utils.HandlerExceptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import static com.bosorio.taskupv2.utils.HandlerExceptions.handleExceptions;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks/{taskId}/notes")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody NoteDTO noteDTO, HttpServletRequest request) {
        Long userId = Long.valueOf(getAuthenticatedUser());
        TaskDTO taskDTO = (TaskDTO) request.getAttribute("taskDTO");
        try {
            noteService.createNote(userId, taskDTO, noteDTO);

            return ResponseEntity.ok().body("Note created successfully");
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    private String getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getDetails();
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
