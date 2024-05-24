package com.example.elearning.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import com.example.elearning.dto.PaymentResDto;
import com.example.elearning.service.PaymentService;
import com.example.elearning.service.UserCourseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/payment")
public class PaymentResultController {
    @Autowired
    PaymentService paymentService;
    @Autowired
    UserCourseService userCourseService;
    @Value("${url.home.fe}")
    private String urlHome;
    @GetMapping("/payment-info")
    public String transaction(HttpServletRequest request, Model model) throws Exception {

        int paymentStatus = paymentService.orderReturn(request);

        if (paymentStatus == 1) {
            model.addAttribute("status", "OK");
            model.addAttribute("message", "Thanh Toán Thành Công");

        } else {
            model.addAttribute("status", "Failed");
            model.addAttribute("message", "Thanh Toán Thất Bại");
        }
        model.addAttribute("url", urlHome);

        return "paymentResult";
    }
}
