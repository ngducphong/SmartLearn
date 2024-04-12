package com.example.elearning.controller;

import com.example.elearning.service.impl.GeminiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/gemini")
public class GeminiController {
    @Autowired
    GeminiServiceImpl geminiService;

    @PostMapping("/chat")
    ResponseEntity<String> chatGemini(@RequestBody String text) {
        return new ResponseEntity<>(geminiService.getCompletion(text), HttpStatus.OK);
    }
}
