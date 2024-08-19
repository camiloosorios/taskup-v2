package com.bosorio.taskupv2.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NoteDTO {

    private Long id;

    private UserDTO createdBy;

    private TaskDTO task;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
