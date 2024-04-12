package com.example.elearning.service.impl;


import com.example.elearning.dto.LessonDto;
import com.example.elearning.exception.CustomException;
import com.example.elearning.model.Chapter;
import com.example.elearning.model.Lesson;
import com.example.elearning.repository.ChapterRepository;
import com.example.elearning.repository.LessonRepository;
import com.example.elearning.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    ChapterRepository chapterRepository;

    public LessonDto save(Lesson entity, LessonDto dto) throws CustomException {

        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setVideo(dto.getVideo());
        entity.setResources(dto.getResources());
        entity.setDocument(dto.getDocument());

        if(dto.getChapterDto() == null || dto.getChapterDto().getId() == null){
            throw new CustomException("Chapter is not null");
        }
        Chapter chapter = chapterRepository.findById(dto.getChapterDto().getId()).orElse(null);
        if (chapter == null){
            throw new CustomException("Chapter is not null");
        }
        entity.setChapter(chapter);
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
}
