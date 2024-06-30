package com.example.elearning.controller;

import com.example.elearning.dto.request.ChangePasswordRequest;
import com.example.elearning.dto.request.EditUserRequest;
import com.example.elearning.dto.request.ResetPasswordRequest;
import com.example.elearning.dto.request.UserInfoRequest;
import com.example.elearning.dto.response.UserResponse;
import com.example.elearning.exception.CustomException;
import com.example.elearning.service.UserService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Secured("ROLE_ADMIN")
    @PostMapping("/register/sub-admin")
    public ResponseEntity<String> registerSubAdmin(@RequestBody @Valid UserInfoRequest userInfoRequest) throws CustomException {
        userService.registerSubAdmin(userInfoRequest);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserInfoRequest userInfoRequest) throws CustomException {
        userService.createUser(userInfoRequest);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserInfoRequest userInfoRequest) throws CustomException {
        userService.registerUser(userInfoRequest);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN", "ROLE_SUBADMIN", "ROLE_USER"})
    @PutMapping("/edit-info-user")
    public ResponseEntity<String> editInfoUser(@RequestBody @Valid EditUserRequest editUserRequest) throws CustomException {
        userService.editInfoUser(editUserRequest);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_SUBADMIN", "ROLE_USER"})
    @PutMapping("/change-password")
    public ResponseEntity<String> changePass(@RequestBody @Valid ChangePasswordRequest request) throws CustomException {
        userService.changePassword(request);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/admin/edit-user/{id}")
    public ResponseEntity<String> adminEditUser(@RequestBody @Valid EditUserRequest editUserRequest, @PathVariable Long id) throws CustomException {
        userService.editUser(editUserRequest, id);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/page")
    ResponseEntity<Page<UserResponse>> findAllUser(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "createDate", required = false) String createDate,
            @RequestParam(value = "voided", required = false) Boolean voided,
            @PageableDefault(page = 0, size = 1000, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws ParseException {
        return new ResponseEntity<>(userService.findAll(name, username, email, phone, pageable, createDate, voided, role), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/change-status/{id}")
    ResponseEntity<Boolean> changeStatus(@PathVariable Long id) throws CustomException {
        return new ResponseEntity<>(userService.changeStatusActiveUser(id), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/get-user-account-registration-data")
    ResponseEntity<Map<Integer, Long>> getUserAccountRegistrationData(@RequestParam(value = "year", required = false, defaultValue = "2024") Integer year) {
        return new ResponseEntity<>(userService.getUserAccountRegistrationData(year), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/get-payment-chart-data")
    ResponseEntity<Map<Integer, Double>> getPaymentChartData(@RequestParam(value = "year", required = false, defaultValue = "2024") Integer year) {
        return new ResponseEntity<>(userService.getPaymentChartData(year), HttpStatus.OK);
    }
}
