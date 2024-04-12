package com.example.elearning.service;

import com.example.elearning.dto.CourseDto;
import com.example.elearning.dto.PaymentInfoDTO;
import com.example.elearning.dto.UserCourseDto;
import com.example.elearning.exception.CustomException;
import com.example.elearning.model.PaymentInFo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface PaymentService {
    ResponseEntity<?> createPayment(HttpServletRequest request, UserCourseDto dto) throws CustomException, UnsupportedEncodingException;

    PaymentInfoDTO savePaymentInfo(PaymentInfoDTO paymentInfoDTO);

    void transaction(Long courseId, String vnp_SecureHash) throws CustomException;

    int orderReturn(HttpServletRequest request) throws CustomException;
}
