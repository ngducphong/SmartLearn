package com.example.elearning.service;

import com.example.elearning.dto.request.*;
import com.example.elearning.dto.response.JwtResponse;
import com.example.elearning.dto.response.UserResponse;
import com.example.elearning.exception.CustomException;
import com.example.elearning.model.Users;
import com.infobip.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.Map;

public interface UserService {
    void registerUser(UserInfoRequest userInfoRequest) throws CustomException;
    void registerSubAdmin(UserInfoRequest request) throws CustomException;
    UserResponse createUser(UserInfoRequest request) throws CustomException;
    JwtResponse login(UserLogin userLogin) throws CustomException;
    String handleLogout(Authentication authentication);

    void editInfoUser(EditUserRequest editUserRequest) throws CustomException;

    void changePassword(ChangePasswordRequest passwordRequest) throws CustomException;

    void editUser(EditUserRequest editUserRequest, Long id) throws CustomException;

    Page<UserResponse> findAll(String name, String username, String email, String phone, Pageable pageable);

    Users getCurrentUser();

    boolean changeStatusActiveUser(Long id) throws CustomException;

    void resetPassword(ResetPasswordRequest request) throws CustomException, IOException, ApiException;

    Map<Integer, Long> getUserAccountRegistrationData(Integer year);
    Map<Integer, Double> getPaymentChartData(Integer year);

}
