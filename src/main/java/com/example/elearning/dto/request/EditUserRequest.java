package com.example.elearning.dto.request;

import com.example.elearning.dto.validation.AtLeastOneNotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AtLeastOneNotBlank(fields = {"phone", "email"})
public class EditUserRequest {
    @NotBlank(message = "fullName not blank")
    private String fullName;
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be only number")
    private String phone;
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String email;
    @NotBlank(message = "username not blank")
    private String username;
    private String password;
    private List<String> role;
    private Boolean voided;
}
