package com.example.elearning.controller.gemini;

import com.example.elearning.dto.gemini.GeminiRecords;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/v1beta/models/")
public interface GeminiInterface {
    @PostExchange("{model}:generateContent")
    GeminiRecords.GeminiResponse getCompletion(
            @PathVariable String model,
            @RequestBody GeminiRecords.GeminiRequest request);
}
