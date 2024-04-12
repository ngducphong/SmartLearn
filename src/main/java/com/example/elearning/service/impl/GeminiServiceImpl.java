package com.example.elearning.service.impl;

import com.example.elearning.controller.gemini.GeminiInterface;
import com.example.elearning.dto.gemini.GeminiRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeminiServiceImpl {
    public static final String GEMINI_PRO = "gemini-pro";
    private final GeminiInterface geminiInterface;

    @Autowired
    public GeminiServiceImpl(GeminiInterface geminiInterface) {
        this.geminiInterface = geminiInterface;
    }

    public GeminiRecords.GeminiResponse getCompletion(GeminiRecords.GeminiRequest request) {
        return geminiInterface.getCompletion(GEMINI_PRO, request);
    }
    public String getCompletion(String text) {
        GeminiRecords.GeminiResponse response = getCompletion(new GeminiRecords.GeminiRequest(
                List.of(new GeminiRecords.Content(List.of(new GeminiRecords.TextPart(text))))));
        return response.candidates().get(0).content().parts().get(0).text();
    }
}
