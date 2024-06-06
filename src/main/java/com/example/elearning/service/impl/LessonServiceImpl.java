package com.example.elearning.service.impl;


import com.example.elearning.dto.LessonDto;
import com.example.elearning.exception.CustomException;
import com.example.elearning.model.Chapter;
import com.example.elearning.model.Lesson;
import com.example.elearning.repository.ChapterRepository;
import com.example.elearning.repository.LessonRepository;
import com.example.elearning.service.LessonService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LessonServiceImpl implements LessonService {
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    ChapterRepository chapterRepository;

    public LessonDto save(Lesson entity, LessonDto dto) throws CustomException {

        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());

        entity.setResources(dto.getResources());
        entity.setDocument(dto.getDocument());
        entity.setVoided(dto.isVoided());
        if(dto.getChapterId() == null && (dto.getChapterDto() == null || dto.getChapterDto().getId() == null)){
            throw new CustomException("Chapter is not null");
        }
        Chapter chapter = null;
        if(dto.getChapterId() != null){
            chapter = chapterRepository.findById(dto.getChapterId()).orElse(null);
        }else if (dto.getChapterDto() != null){
            chapter = chapterRepository.findById(dto.getChapterDto().getId()).orElse(null);
        }


        if (chapter == null){
            throw new CustomException("Chapter is not null");
        }
        entity.setChapter(chapter);
        entity = this.uploadFileVideo(dto, entity);
        entity = lessonRepository.save(entity);

        return new LessonDto(entity);
    }

    @Override
    public LessonDto saveLesson(LessonDto dto) throws CustomException {
        Lesson entity = new Lesson();
        return this.save(entity, dto);
    }


    @Override
    public LessonDto upDateLesson(LessonDto dto, Long id) throws CustomException {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new CustomException("Lesson not found"));
        return this.save(lesson, dto);
    }


    @Override
    public void deleteLesson(Long id) throws CustomException {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new CustomException("Lesson not found") );
        if(lesson.getVoided() == null || lesson.getVoided() == false){
            lesson.setVoided(true);
        }else {
            lesson.setVoided(false);
        }
        lessonRepository.save(lesson);
    }

    @Override
    public List<LessonDto> getAllLesson() {
        return lessonRepository.getAll();
    }

    @Override
    public LessonDto getLessonDtoById(Long id) throws CustomException {
        return new LessonDto(this.getLessonById(id));
    }
    private Lesson getLessonById(Long id) throws CustomException {
        Optional<Lesson> optional = lessonRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new CustomException("Lesson not found");
    }

    @Override
    public Page<LessonDto> pagingLessonDto(Pageable pageable, String title) {
        Page<LessonDto> page = lessonRepository.getLessonPage(pageable, title);
        return page;
    }

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("smart-learn-e5bc7.appspot.com", fileName); // Replace with your bucker name
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        InputStream inputStream = LessonServiceImpl.class.getClassLoader().getResourceAsStream("smart-learn-e5bc7-firebase-adminsdk-8lvgz-c46fc6be8c.json"); // change the file name with your one
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/smart-learn-e5bc7.appspot.com/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    private Lesson uploadFileVideo(LessonDto dto, Lesson entity) throws CustomException {
        if(dto.getVideoFile() != null && !dto.getVideoFile().isEmpty()){
            try {
                String fileName = dto.getVideoFile().getOriginalFilename();                        // to get original file name
                fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

                File file = this.convertToFile(dto.getVideoFile(), fileName);                      // to convert multipartFile to File
                String URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
                file.delete();

                entity.setVideo(URL);
            } catch (Exception e) {
                throw new CustomException(e.getMessage());
            }
        }
        return entity;
    }
}
