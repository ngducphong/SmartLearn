package com.example.elearning.service;

import com.example.elearning.dto.CourseDto;
import com.example.elearning.dto.PaymentInfoDTO;
import com.example.elearning.dto.UserCourseDto;
import com.example.elearning.exception.CustomException;
import com.example.elearning.model.PaymentInFo;
import com.example.elearning.model.Users;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface PaymentService {
    ResponseEntity<?> createPayment(HttpServletRequest request, UserCourseDto dto) throws Exception;

    PaymentInfoDTO savePaymentInfo(PaymentInfoDTO paymentInfoDTO, Users users);

    void transaction(Long courseId, String username, String vnp_SecureHash) throws CustomException;

    int orderReturn(HttpServletRequest request) throws Exception;
}
