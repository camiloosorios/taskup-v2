package com.bosorio.taskupv2.controllers;

import com.bosorio.taskupv2.DTOs.UpdateCurrentPasswordDTO;
import com.bosorio.taskupv2.DTOs.UserDTO;
import com.bosorio.taskupv2.services.UserService;
import com.bosorio.taskupv2.utils.HandlerExceptions;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
            String message = userService.login(userDTO);

            return ResponseEntity.ok().body(message);
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser() {
        Long userId = Long.valueOf(getAuthenticatedUser());
        try {
            UserDTO userDTO = userService.getUser(userId);

            return ResponseEntity.ok().body(userDTO);
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

    @PostMapping("/update-password/{token}")
    public ResponseEntity<?> updatePassword(@PathVariable String token, @RequestBody UserDTO userDTO) {
        try {
            userService.updatePassword(token, userDTO);

            return ResponseEntity.ok().body("Password updated successfully");
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updateCurrentPassword(@Valid @RequestBody UpdateCurrentPasswordDTO updateCurrentPassword) {
        Long userId = Long.parseLong(getAuthenticatedUser());
        try {
            userService.updateCurrentPassword(userId, updateCurrentPassword);

            return ResponseEntity.ok().body("Password updated successfully");
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @PostMapping("/check-password")
    public ResponseEntity<?> checkPassword(@RequestBody UserDTO userDTO) {
        Long userId = Long.parseLong(getAuthenticatedUser());
        try {
            userService.checkPassword(userId, userDTO);

            return ResponseEntity.ok().body("Password check successfully");
        } catch (RuntimeException e) {
            return handleExceptions(e);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UserDTO userDTO) {
        Long userId = Long.valueOf(getAuthenticatedUser());
        try {
            userService.updateProfile(userId, userDTO);

            return ResponseEntity.ok().body("Profile updated successfully");
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
