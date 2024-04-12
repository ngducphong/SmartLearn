package com.example.elearning.dto.request;

import com.example.elearning.dto.validation.AtLeastOneNotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AtLeastOneNotBlank(fields = {"phone", "email"})
public class ResetPasswordRequest {
    @Email(message = "Email không đúng định dạng")
    private String email;
//    @NotNull(message = "Tên tài khoản không được để trống")
//    private String username;
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be only number")
    private String phone;
}
