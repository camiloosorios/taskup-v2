package com.bosorio.taskupv2.controllers;

import com.bosorio.taskupv2.DTOs.UserDTO;
import com.bosorio.taskupv2.services.UserService;
import com.bosorio.taskupv2.utils.HandlerExceptions;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import static com.bosorio.taskupv2.utils.HandlerExceptions.handleExceptions;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-account")
    public ResponseEntity<?> createAccount(@Valid @RequestBody UserDTO userDTO) {
        try {
            userService.createUser(userDTO);

            return ResponseEntity.ok().body("User created successfully");
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            userService.login(userDTO);

            return ResponseEntity.ok().body("User logged in successfully");
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
