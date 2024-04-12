package com.example.elearning.config;

import com.example.elearning.constant.ConstantVariable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${course.file.path.img}")
    private String filePath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler(ConstantVariable.urlImg+"**")
                .addResourceLocations("file:///"+filePath);
        System.out.println(filePath);
    }
}
