package com.bosorio.taskupv2.utils;

import com.bosorio.taskupv2.Exceptions.BadRequestException;
import com.bosorio.taskupv2.Exceptions.ForbbidenException;
import com.bosorio.taskupv2.Exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerExceptions {
    public static ResponseEntity<?> handleExceptions(RuntimeException e) {
        Map<String, String> message = new HashMap<>();
        message.put("error", e.getMessage());
        if (e instanceof ForbbidenException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
        } else if (e instanceof NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (e instanceof BadRequestException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    public static ResponseEntity<?> handleEmptyBody() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Request Body cannot be empty");

        return ResponseEntity.badRequest().body(response);
    }

    public static ResponseEntity<?> handleValidations(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        Map<String, Object> response = new HashMap<>();
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }
}
