package com.bosorio.taskupv2.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TaskDTO {

    private Long id;

    @NotBlank(message = "Task Name must not be blank")
    private String name;

    @NotBlank(message = "Description must not be blank")
    private String description;

    @NotBlank(message = "Status must not be blank")
    private String status;

    private ProjectDTO project;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
