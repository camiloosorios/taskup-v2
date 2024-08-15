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

import java.util.Map;

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

    @PostMapping("/confirm-account")
    public ResponseEntity<?> confirmAccount(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        try {
            userService.confirmAccount(token);

            return ResponseEntity.ok().body("Account confirmed successfully");
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

    @PostMapping("/request-code")
    public ResponseEntity<?> requestConfirmationCode(@RequestBody UserDTO userDTO) {
        try {
            userService.sendConfirmationCode(userDTO);

            return ResponseEntity.ok().body("Confirmation code was sent to your email address");
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody UserDTO userDTO) {
        try {
            userService.resetPassword(userDTO);

            return ResponseEntity.ok().body("A new token was sent to your email address");
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        try {
            userService.validateToken(token);

            return ResponseEntity.ok().body("Token validated successfully");
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
