package com.example.elearning.controller;


import com.example.elearning.dto.CourseDto;
import com.example.elearning.dto.PaymentInfoDTO;
import com.example.elearning.dto.PaymentResDto;
import com.example.elearning.dto.UserCourseDto;
import com.example.elearning.exception.CustomException;
import com.example.elearning.service.CourseService;
import com.example.elearning.service.PaymentService;
import com.example.elearning.service.UserCourseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Objects;


@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;
    @Autowired
    UserCourseService userCourseService;


    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(HttpServletRequest request, @RequestBody UserCourseDto dto) throws Exception {
        return paymentService.createPayment(request, dto);
    }

}
