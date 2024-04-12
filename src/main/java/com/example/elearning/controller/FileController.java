package com.example.elearning.controller;

import com.example.elearning.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/v1/file")
@RestController
@Secured("ROLE_ADMIN")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload-file")
    ResponseEntity<String> uploadFile(@ModelAttribute MultipartFile file) throws IOException {
        return new ResponseEntity<>(fileService.uploadFile(file), HttpStatus.OK);
    }

    @PostMapping("/upload-multyFile")
    ResponseEntity<List<String>> uploadMultyFile(MultipartFile [] files) throws IOException {
        return new ResponseEntity<>(fileService.uploadMultyFile(files), HttpStatus.OK);
    }
}
