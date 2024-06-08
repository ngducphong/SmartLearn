package com.example.elearning.dto;


import com.example.elearning.dto.base.BaseObjectDto;
import com.example.elearning.model.Lesson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Getter
@Setter
public class LessonDto  extends BaseObjectDto {
    private String keyVideo = "ngducphong010402";

    private String title;
    private String video;
    private String resources;
    private String description;
    private ChapterDto chapterDto;
    private Long chapterId;
    private String document;
    private MultipartFile videoFile;

    public LessonDto() {
    }

    public LessonDto(Lesson entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.video = entity.getVideo();
        this.resources = entity.getResources();
        this.description = entity.getDescription();
        this.document =entity.getDocument();
        if (entity.getVoided() != null) {
            this.voided = entity.getVoided();
        }
        if(entity.getChapter() != null){
//            this.chapterDto = new ChapterDto(entity.getChapter());
            this.chapterId = entity.getChapter().getId();
        }
    }

    public LessonDto(Lesson entity, Boolean isGetFull) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.video = entity.getVideo();
        this.resources = entity.getResources();
        this.description = entity.getDescription();
        this.document =entity.getDocument();
        if(entity.getChapter() != null){
//            this.chapterDto = new ChapterDto(entity.getChapter());
            this.chapterId = entity.getChapter().getId();
        }
        if (entity.getVoided() != null) {
            this.voided = entity.getVoided();
        }
        if (isGetFull) {
            this.createDate = entity.getCreateDate();
            this.modifyBy = entity.getModifyBy();
            this.createBy = entity.getCreateBy();
            this.modifyDate = entity.getModifyDate();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if(video != null){
            // Khởi tạo khóa bí mật
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyVideo.getBytes(), "AES");

            // Tạo đối tượng Cipher
            Cipher cipher = Cipher.getInstance("AES");

            // Khởi tạo Cipher ở chế độ mã hóa
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            // Mã hóa chuỗi
            byte[] encryptedBytes = cipher.doFinal(video.getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ChapterDto getChapterDto() {
        return chapterDto;
    }

    public void setChapterDto(ChapterDto chapterDto) {
        this.chapterDto = chapterDto;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public MultipartFile getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(MultipartFile videoFile) {
        this.videoFile = videoFile;
    }
}
