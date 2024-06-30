package com.example.elearning.repository;


import com.example.elearning.dto.CourseDto;
import com.example.elearning.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("select new com.example.elearning.dto.CourseDto(e, true) from Course e  ")
    List<CourseDto> getAll();

    @Query("select new com.example.elearning.dto.CourseDto(e, true) from Course e " +
            "where ((:home is null OR (e.voided is null or e.voided = false)) " +
            "AND (:title is null or e.title like concat('%', :title, '%')) )" +
            "AND (e.voided = :voided or :voided is null) " +
            "AND (DATE(e.createDate) = DATE(:searchDate) OR :searchDate is null) " +
            "AND (COALESCE(:courseIds, NULL) IS NULL OR e.category.id IN (:courseIds)) " +
            "AND ((COALESCE(e.price, 0) >= COALESCE(:priceFrom, 0) OR :priceFrom is null) " +
            "OR (:priceFrom = 0 AND e.price is null)) " +
            "AND ((COALESCE(e.price, 9999999999) <= COALESCE(:priceTo, 9999999999) OR :priceTo is null) " +
            "OR (:priceTo = 9999999999 AND e.price is null))" +
            "")
    Page<CourseDto> getCoursePage(Pageable pageable,
                                  @Param("title") String title,
                                  @Param("home") String home,
                                  @Param("searchDate") Date searchDate,
                                  @Param("priceFrom") Double priceFrom,
                                  @Param("priceTo") Double priceTo,
                                  @Param("voided") Boolean voided,
                                  List<Long> courseIds
    );

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
            "    cast((res.id is not null) as signed)  \n" +
            " from course e left join ( select e2.id, u.user_id as user_id from course e2 left join user_course u on e2.id = u.course_id where u.user_id = :idUser )" +
            " res on e.id = res.id" +
            " where (e.voided is null or e.voided = false) " +
            " AND (:title is null or e.title like concat('%', :title, '%')) " +
            " AND ((COALESCE(e.price, 0) >= COALESCE(:priceFrom, 0) OR :priceFrom is null) OR (:priceFrom = 0 AND e.price is null)) " +
            " AND (COALESCE(e.price, 9999999999) <= COALESCE(:priceTo, 9999999999) OR :priceTo is null OR (:priceTo = 9999999999 AND e.price is null)) " +
            " AND (DATE(e.create_date) = DATE(:searchDate) OR :searchDate is null) " +
            " AND (COALESCE(:courseIds) IS NULL OR e.category_id IN (:courseIds)) " +
            " AND (e.voided = :voided or :voided is null)",
            countQuery = "select count(*) from course e left join ( select e2.id, u.user_id as user_id from course e2 left join user_course u on e2.id = u.course_id where u.user_id = :idUser )" +
                    " res on e.id = res.id" +
                    " where (e.voided is null or e.voided = false) " +
                    " AND (:title is null or e.title like concat('%', :title, '%')) " +
                    " AND ((COALESCE(e.price, 0) >= COALESCE(:priceFrom, 0) OR :priceFrom is null) OR (:priceFrom = 0 AND e.price is null)) " +
                    " AND (COALESCE(e.price, 9999999999) <= COALESCE(:priceTo, 9999999999) OR :priceTo is null OR (:priceTo = 9999999999 AND e.price is null)) " +
                    " AND (DATE(e.create_date) = DATE(:searchDate) OR :searchDate is null) " +
                    " AND (COALESCE(:courseIds) IS NULL OR e.category_id IN (:courseIds)) " +
                    " AND (e.voided = :voided or :voided is null)",
            nativeQuery = true)
    Page<Object[]> getCoursePageLogIn(Pageable pageable, @Param("title") String title, @Param("idUser") Long idUser,
                                      @Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo,
                                      @Param("searchDate") Date searchDate, @Param("voided") Boolean voided, @Param("courseIds") List<Long> courseIds);


    @Query("SELECT new com.example.elearning.dto.CourseDto(e, true, u.isFavourite) " +
            "FROM Course e " +
            "LEFT JOIN UserCourse u ON e.id = u.course.id " +
            "WHERE (e.voided IS NULL OR e.voided = FALSE) " +
            "AND u.users.id = :idUser " +
            "AND (:title IS NULL OR e.title LIKE CONCAT('%', :title, '%')) " +
            "AND (:priceFrom IS NULL OR (COALESCE(e.price, 0) >= :priceFrom)) " +
            "AND (:priceTo IS NULL OR (COALESCE(e.price, 0) <= :priceTo)) " +
            "AND (:courseIds IS NULL OR e.category.id IN :courseIds)")
    Page<CourseDto> getCourseByUser(Pageable pageable, Long idUser, String title, List<Long> courseIds, Double priceFrom, Double priceTo);

    @Query("select new com.example.elearning.dto.CourseDto(e , true, u.isFavourite)" +
            " from Course e left join UserCourse u on e.id = u.course.id " +
            " where (e.voided is null or e.voided = false) and u.users.id = :idUser" +
            " AND (:title is null or  e.title like concat('%',:title,'%')) " +
            "")
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
