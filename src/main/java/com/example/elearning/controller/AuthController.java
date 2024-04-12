package com.example.elearning.controller;

import com.example.elearning.dto.request.ResetPasswordRequest;
import com.example.elearning.dto.request.UserLogin;
import com.example.elearning.dto.response.JwtResponse;
import com.example.elearning.exception.CustomException;
import com.example.elearning.service.UserService;
import com.infobip.ApiException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> handleLogin(@RequestBody UserLogin userLogin) throws CustomException {
        return new ResponseEntity<>(userService.login(userLogin), HttpStatus.OK);
    }

//    @PostMapping("/register")
//    public ResponseEntity<String> handleRegister(@RequestBody UserRegister userRegister) {
//        userService.register(userRegister);
//        return new ResponseEntity<>("success",HttpStatus.CREATED);
//    }

//    @PostMapping("/refreshToken")
//    public ResponseEntity<JwtResponse> handleRefreshToken(HttpServletRequest request, HttpServletResponse response) {
//        return new ResponseEntity<>(userService.handleRefreshToken(request, response), HttpStatus.OK);
//    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> handleLogout(Authentication authentication) {
        return new ResponseEntity<>(userService.handleLogout(authentication), HttpStatus.OK);
    }


    @PostMapping("/reset-password")
    ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) throws CustomException, IOException, ApiException {
        userService.resetPassword(request);
        return new ResponseEntity<>("Success",HttpStatus.OK);
    }


}