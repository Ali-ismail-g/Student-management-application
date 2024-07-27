package com.app.studentManagementSystem.model.request;

import com.app.studentManagementSystem.entity.Role;
import com.app.studentManagementSystem.util.EnumValidator;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "the username is required.")
    @Size(min=3,max=20,message = "the username must be between 3 and 20 characters.")
    private String userName;
    @NotEmpty(message = "the email field is required.")
    @Email(message = "the email is not valid.")
    private String email;
    @NotBlank(message = "The password is required.")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$", message = "Password must be 8 characters long and combination of uppercase letters, lowercase letters, numbers, special characters.")
    private String password;
    @EnumValidator(acceptedValues = {"admin","student"})
    public Role role;
}
