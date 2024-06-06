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
            + " Where ((:home is null OR (e.voided is null or e.voided = false)) AND (:title is null or  e.title like concat('%',:title,'%')) )")
    Page<CourseDto> getCoursePage(Pageable pageable, String title, String home);

    @Query(value = "select " +
            "    e.id,\n" +
            "    e.create_date,\n" +
            "    e.modify_date,\n" +
            "    e.create_by,\n" +
            "    e.modify_by,\n" +
            "    e.voided,\n" +
            "    e.title,\n" +
            "    e.image,\n" +
            "    e.price,\n" +
            "    e.description,\n" +
            "    e.sub_description, \n" +
            "    0, \n" +
            "    0 ,  \n" +
            "    res.id is not null  \n" +
            " from course e left join ( select e2.id, u.user_id as user_id from course e2 left join user_course u on e2.id = u.course_id where u.user_id = :idUser )" +
            " res on e.id = res.id" +
            " where (e.voided is null or e.voided = false) " +
            " AND (:title is null or  e.title like concat('%',:title,'%')) ", nativeQuery = true)
    Page<Object[]> getCoursePageLogIn(Pageable pageable, String title, Long idUser);


    @Query("select new com.example.elearning.dto.CourseDto(e , true, u.isFavourite)" +
            " from Course e left join UserCourse u on e.id = u.course.id " +
            " where (e.voided is null or e.voided = false) and u.users.id = :idUser" +
            " AND (:title is null or  e.title like concat('%',:title,'%')) ")
    Page<CourseDto> getCourseByUser(Pageable pageable, Long idUser, String title);

    @Query(value = "SELECT \n" +
            "    e.id,\n" +
            "    e.create_date,\n" +
            "    e.modify_date,\n" +
            "    e.create_by,\n" +
            "    e.modify_by,\n" +
            "    e.voided,\n" +
            "    e.title,\n" +
            "    e.image,\n" +
            "    e.price,\n" +
            "    e.description,\n" +
            "    e.sub_description, \n" +
            "    COALESCE(uc.sum, 0) AS sumUserCourse, \n" +
            "    COALESCE(cc.sumChapter, 0) AS sumChapter\n" +
            "FROM \n" +
            "    course e\n" +
            "LEFT JOIN (\n" +
            "    SELECT \n" +
            "        u.course_id, \n" +
            "        COUNT(u.id) AS sum\n" +
            "    FROM \n" +
            "        user_course u\n" +
            "    GROUP BY \n" +
            "        u.course_id\n" +
            ") uc ON e.id = uc.course_id\n" +
            "LEFT JOIN (\n" +
            "    SELECT \n" +
            "        c.course_id, \n" +
            "        COUNT(c.id) AS sumChapter\n" +
            "    FROM \n" +
            "        chapter c\n" +
            "    GROUP BY \n" +
            "        c.course_id\n" +
            ") cc ON e.id = cc.course_id\n" +
            "ORDER BY \n" +
            "    sumUserCourse DESC;", nativeQuery = true)
    Page<Object[]> getCourseMostRegistered(Pageable pageable);

    @Query(value = "SELECT \n" +
            "    e.id,\n" +
            "    e.create_date,\n" +
            "    e.modify_date,\n" +
            "    e.create_by,\n" +
            "    e.modify_by,\n" +
            "    e.voided,\n" +
            "    e.title,\n" +
            "    e.image,\n" +
            "    e.price,\n" +
            "    e.description,\n" +
            "    e.sub_description, \n" +
            "    COALESCE(uc.sum, 0) AS sumUserCourse, \n" +
            "    COALESCE(cc.sumChapter, 0) AS sumChapter,\n" +
            "    COALESCE(ucf.sumFavourite, 0) AS sumFavourite " +
            " FROM \n" +
            "    course e\n" +
            "LEFT JOIN (\n" +
            "    SELECT \n" +
            "        u.course_id, \n" +
            "        COUNT(u.id) AS sum\n" +
            "    FROM \n" +
            "        user_course u\n" +
            "    GROUP BY \n" +
            "        u.course_id\n" +
            ") uc ON e.id = uc.course_id\n" +
            " LEFT JOIN (\n" +
            "    SELECT \n" +
            "        u.course_id, \n" +
            "        COUNT(u.id) AS sumFavourite\n" +
            "    FROM \n" +
            "        user_course u\n" +
            "        where u.is_favourite is true\n" +
            "    GROUP BY \n" +
            "        u.course_id\n" +
            ") ucf ON e.id = ucf.course_id " +
            "LEFT JOIN (\n" +
            "    SELECT \n" +
            "        c.course_id, \n" +
            "        COUNT(c.id) AS sumChapter\n" +
            "    FROM \n" +
            "        chapter c\n" +
            "    GROUP BY \n" +
            "        c.course_id\n" +
            ") cc ON e.id = cc.course_id\n" +
            "ORDER BY \n" +
            "    sumFavourite DESC;", nativeQuery = true)
    Page<Object[]> getCourseFavourite(Pageable pageable);
}
