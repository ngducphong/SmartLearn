package com.example.elearning.service.impl;


import com.example.elearning.dto.CourseDto;
import com.example.elearning.exception.CustomException;
import com.example.elearning.model.Category;
import com.example.elearning.model.Chapter;
import com.example.elearning.model.Course;
import com.example.elearning.model.Users;
import com.example.elearning.repository.*;
import com.example.elearning.security.user_principal.UserPrincipal;
import com.example.elearning.service.CourseService;
import com.example.elearning.service.UserService;
import com.example.elearning.utills.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    UserCourseRepository userCourseRepository;
    @Autowired
    ChapterRepository chapterRepository;

    @Value("${course.file.path.img}")
    private String filePath;

    @Autowired
    UserService iUserService;

    public CourseDto save(Course entity, CourseDto dto) throws IOException, CustomException {

        if (dto.getCategoryId() == null) {
            throw new CustomException("CategoryId is not null");
        }
        Category category = categoryRepository.findById(Long.valueOf(dto.getCategoryId())).orElse(null);
        if (category == null) {
            throw new CustomException("Category is null");
        }
        entity.setCategory(category);

        entity.setVoided(dto.isVoided());
        entity.setTitle(dto.getTitle());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        entity.setSubDescription(dto.getSubDescription());
        entity = this.uploadFileImg(dto, entity);
        entity = courseRepository.save(entity);
        return new CourseDto(entity);
    }

    // upload file img
    private Course uploadFileImg(CourseDto dto, Course entity) throws IOException {
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            LocalDateTime dateTime = LocalDateTime.now();
            String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-nnnnnnnnn"));
            String filename = formattedDateTime + "_" + removeAccentsAndSpaces(dto.getImageFile().getOriginalFilename());

            entity.setImage(filename);

            // ghi file
            byte[] bytes = dto.getImageFile().getBytes();
            Path path = Paths.get(filePath + filename);
            Files.write(path, bytes);

        }
        return entity;
    }

    private String removeAccentsAndSpaces(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(temp).replaceAll("");
        result = result.replaceAll("đ", "d").replaceAll("Đ", "D");
        result = result.replaceAll("\\s+", "");
        return result;
    }

    @Override
    public CourseDto saveCourse(CourseDto dto) throws IOException, CustomException {
        Course course = new Course();
        return this.save(course, dto);
    }

    @Override
    public CourseDto upDateCourse(CourseDto dto, Long id) throws CustomException, IOException {
        Course course = courseRepository.findById(id).orElseThrow(() -> new CustomException("Course not found"));
        return this.save(course, dto);
    }


    @Override
    public void deleteCourse(Long id) throws CustomException {
        Course course = courseRepository.findById(id).orElseThrow(() -> new CustomException("Course not found"));
        if (course.getVoided() == null || course.getVoided() == false) {
            course.setVoided(true);
        } else {
            course.setVoided(false);
        }
        courseRepository.save(course);
    }

    @Override
    public List<CourseDto> getAllCourse() {
        return courseRepository.getAll();
    }

    @Override
    public CourseDto getCourseDtoById(Long id) throws CustomException {
        return new CourseDto(this.getCourseById(id));
    }

    @Override
    public CourseDto getFullCourse(Long id) throws CustomException {
        CourseDto courseDto = new CourseDto(this.getCourseById(id));
        courseDto.setTotalChapter(chapterRepository.countChapterByCourseId(id));
        courseDto.setTotalLesson(lessonRepository.countLessonByCourseId(id));
        courseDto.setTotalUser(userCourseRepository.countUserCourseByCourseId(id, false));
        courseDto.setTotalFavourite(userCourseRepository.countUserCourseByCourseId(id, true));
        return courseDto;
    }

    private Course getCourseById(Long id) throws CustomException {
        if(!this.checkRegisterCourse(id)){
            throw new CustomException("The course has not been registered");
        }
        Optional<Course> optional = courseRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new CustomException("Course not found");
    }

    @Override
    public Page<CourseDto> pagingCourseDto(Pageable pageable, String title, String home) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Page<Object[]> results = courseRepository.getCoursePageLogIn(pageable, title, userPrincipal.getId());
            return results.map(result -> convertToCourseDto(result, false, true));
        }
        Page<CourseDto> page = courseRepository.getCoursePage(pageable, title, home);
        return page;
    }

    @Override
    public Page<CourseDto> pagingCourseMostRegistered(Pageable pageable) {
        Page<Object[]> results = courseRepository.getCourseMostRegistered(pageable);
        return results.map(result -> convertToCourseDto(result, false, false));
    }

    @Override
    public Page<CourseDto> pagingCourseFavourite(Pageable pageable) {
        Page<Object[]> results = courseRepository.getCourseFavourite(pageable);
        return results.map(result -> convertToCourseDto(result, true, false));
    }

    private CourseDto convertToCourseDto(Object[] objects, Boolean isFavourite, Boolean isLogin) {
        CourseDto dto = new CourseDto();
        dto.setId((Long) objects[0]); // Giả sử id là phần tử đầu tiên
        dto.setCreateDate((Date) objects[1]);
        dto.setModifyDate((Date) objects[2]);
        dto.setCreateBy((String) objects[3]); // Giả sử createBy là phần tử thứ tư
        dto.setModifyBy((String) objects[4]); // Giả sử modifyBy là phần tử thứ năm
        dto.setVoided((Boolean) objects[5]); // Giả sử voided là phần tử thứ sáu
        dto.setTitle((String) objects[6]); // Giả sử title là phần tử thứ bảy
        dto.setImage((String) objects[7]); // Giả sử image là phần tử thứ tám
        dto.setPrice((Double) objects[8]); // Giả sử price là phần tử thứ chín
        dto.setDescription((String) objects[9]); // Giả sử description là phần tử thứ mười
        dto.setSubDescription((String) objects[10]); // Giả sử subDescription là phần tử thứ mười một
        dto.setTotalChapter((Long) objects[12]); // sumChapter là phần tử cuối cùng
        dto.setTotalUser((Long) objects[11]); // sumUserCourse là phần tử áp chót
        if (isFavourite) {
            dto.setTotalFavourite((Long) objects[objects.length - 1]); // Tổng số yêu thích chưa được cung cấp trong truy vấn gốc
        }
        if (isLogin) {
            Long a = (Long) objects[objects.length - 1];
            dto.setIsRegister(a == 1);
        }
        return dto;
    }

    @Override
    public Page<CourseDto> getAllMyCourseDto(Pageable pageable, String title) throws CustomException {
        Users users = iUserService.getCurrentUser();
        if (users == null || users.getId() == null) {
            throw new CustomException("User not found");
        }
        return courseRepository.getCourseByUser(pageable, users.getId(), title);
    }
    private Boolean checkRegisterCourse(Long courseId) {
        Users users = iUserService.getCurrentUser();
        // get list courseId by userId
        List<Long> courseIds = userCourseRepository.getListCourseIdByUsersId(users.getId());
        return courseIds.contains(courseId);
    }
}
