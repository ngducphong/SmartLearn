package com.example.elearning.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    String uploadFile(MultipartFile file) throws IOException;
    List<String> uploadMultyFile(MultipartFile[] files) throws IOException;
}
