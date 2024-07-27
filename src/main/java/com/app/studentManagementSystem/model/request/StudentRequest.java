package com.app.studentManagementSystem.model.request;

import com.app.studentManagementSystem.entity.Student;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest {
    @NotBlank(message = "the firstName is required.")
    @Size(min=3,max=20,message = "the firstName must be between 3 and 20 characters.")
    private String firstName;
    @NotBlank(message = "the lastName is required.")
    @Size(min=3,max=20,message = "the lastName must be between 3 and 20 characters.")
    private String lastName;
    @NotEmpty(message = "the email field is required.")
    @Email(message = "the email is not valid.")
    private String email;
    @NotNull(message = "Date of birth must not be null and must be on this pattern {yyyy-MM-dd}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String jwtToken;
}
