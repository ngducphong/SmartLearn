package com.example.elearning;

import com.example.elearning.controller.gemini.GeminiInterface;
import com.example.elearning.service.impl.GeminiServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ELearningApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    GeminiServiceImpl geminiServiceImpl;
    @Test
    void getCompletion_HHGtTG_question() {
        String text = geminiServiceImpl.getCompletion("""
            hi
            """);
        assertNotNull(text);
        System.out.println(text);
    }

}
