package com.example.elearning.repository;


import com.example.elearning.dto.CourseDto;
import com.example.elearning.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("select new com.example.elearning.dto.CourseDto(e, true) from Course e  ")
    List<CourseDto> getAll();

    @Query("select new com.example.elearning.dto.CourseDto(e, true) from Course e"
            + " Where ((:home is null OR (e.voided is null or e.voided = false)) AND (:title is null or  e.title like concat('%',:title,'%')))")
    Page<CourseDto> getCoursePage(Pageable pageable, String title,String home);

    @Query("select new com.example.elearning.dto.CourseDto(e , true)" +
            " from Course e left join UserCourse u on e.id = u.course.id " +
            " where (e.voided is null or e.voided = false) and u.users.id = :idUser")
    List<CourseDto> getCourseByUser(Long idUser);
}
