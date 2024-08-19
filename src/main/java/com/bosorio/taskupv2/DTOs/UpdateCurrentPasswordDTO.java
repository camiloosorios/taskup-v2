package com.bosorio.taskupv2.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateCurrentPasswordDTO {

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Length(min = 6, message = "Password length must be at least 6 characters")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    private String passwordConfirmation;
}
