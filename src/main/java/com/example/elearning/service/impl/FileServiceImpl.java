package com.example.elearning.service.impl;


import com.example.elearning.constant.ConstantVariable;
import com.example.elearning.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class FileServiceImpl implements FileService {

    @Value("${course.file.path.img}")
    private String filePath;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String filename = "";
        if (file != null && !file.isEmpty()) {
            String nameExtensionFile = "";
            int index = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf('.');
            if (index > 0) {
                nameExtensionFile = file.getOriginalFilename().substring(index + 1);
            }
            filename = new Date().getTime() + "." + nameExtensionFile;
            System.out.println(filename);
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filePath + filename);
            Files.write(path, bytes);
        }
        return ConstantVariable.urlImg + filename;
    }

    @Override
    public List<String> uploadMultyFile(MultipartFile[] files) throws IOException {
        List<String> filenames = new ArrayList<>();
        for (MultipartFile file : files) {
            filenames.add(this.uploadFile(file));
        }
        return filenames;
    }
}
