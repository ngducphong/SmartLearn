package com.example.elearning.dto.request;

import com.example.elearning.dto.validation.AtLeastOneNotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@AtLeastOneNotBlank(fields = {"phone", "email", "username"})
public class UserLogin {
//    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be only number")
//    private String phone;
//    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
//    private String email;
    @NotBlank(message = "username is not blank")
    private String username;
    @NotBlank(message = "password is not blank")
    private String password;
}
