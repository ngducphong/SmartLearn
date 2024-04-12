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
    public ResponseEntity<?> createPayment(HttpServletRequest request, @RequestBody UserCourseDto dto) throws CustomException, UnsupportedEncodingException {
        return paymentService.createPayment(request, dto);
    }

    @GetMapping("/payment-info")
    public ResponseEntity<?> transaction(HttpServletRequest request) throws CustomException {

        int paymentStatus = paymentService.orderReturn(request);

        PaymentResDto paymentResDto = new PaymentResDto();
        if (paymentStatus == 1) {
            paymentResDto.setStatus("Ok");
            paymentResDto.setMessage("Successfully");

        } else {
            paymentResDto.setStatus("Failed");
            paymentResDto.setMessage("Failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(paymentResDto);
    }
}
