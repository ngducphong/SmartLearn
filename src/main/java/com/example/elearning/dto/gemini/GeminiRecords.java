package com.example.elearning.dto.gemini;

import java.util.List;

public class GeminiRecords {
    public record GeminiRequest(List<Content> contents) {}
    public record Content(List<Part> parts) {}

    public sealed interface Part
            permits TextPart, InlineDataPart {
    }

    public record TextPart(String text) implements Part {}

    public record InlineDataPart(InlineData inlineData) implements Part {}
    public record InlineData(String mimeType, String data) { }
    public record GeminiResponse(List<Candidate> candidates,
                                 PromptFeedback promptFeedback) {
        public record Candidate(Content content,
                                String finishReason,
                                int index,
                                List<SafetyRating> safetyRatings) {
            public record Content(List<TextPart> parts, String role) { }
        }
    }
    public record SafetyRating(String category, String probability) { }
    public record PromptFeedback(List<SafetyRating> safetyRatings) { }

}
