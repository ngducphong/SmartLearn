package com.example.elearning.repository;

import com.example.elearning.dto.UserCourseDto;
import com.example.elearning.model.UserCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCourseRepository  extends JpaRepository<UserCourse,Long> {
    @Query("select new com.example.elearning.dto.UserCourseDto(e, true) from UserCourse e  ")
    List<UserCourseDto> getAll();

    @Query("select new com.example.elearning.dto.UserCourseDto(e, true) from UserCourse e " +
            " where e.users.id = :usersId and e.course.id = :courseId")
    List<UserCourseDto> getByUsersIdAndCourseId(Long usersId, Long courseId);

    @Query("select e from UserCourse e " +
            " where e.users.id = :usersId and e.course.id = :courseId")
    List<UserCourse> getByUsersAndCourse(Long usersId, Long courseId);

    @Query("select count(e) from UserCourse e " +
            " where e.course.id = :courseId " +
            " and (:isFavourite is false or e.isFavourite = true ) ")
    Long countUserCourseByCourseId(Long courseId, Boolean isFavourite);
}
