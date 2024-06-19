package com.example.elearning.repository;


import com.example.elearning.dto.CommentDto;
import com.example.elearning.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("select new com.example.elearning.dto.CommentDto(e, true) from Comment e  ")
    List<CommentDto> getAll();

    @Query("select new com.example.elearning.dto.CommentDto(e, true) from Comment e ")
    Page<CommentDto> getCommentPage(Pageable pageable);

    @Query("select new com.example.elearning.dto.CommentDto(e, true) from Comment e" +
            " where  ( e.comment is null )")
    List<CommentDto> getCommentParent();

    @Query("select new com.example.elearning.dto.CommentDto(e, true) from Comment e" +
            " where  ( :parentId is null or e.comment.id = :parentId )  order by e.createDate desc")
    List<CommentDto> getCommentChildrenByParentId( Long parentId);

    @Query("select new com.example.elearning.dto.CommentDto(e, true) from Comment e" +
            " where  (  e.lesson.id = :lessonId ) and e.comment is null order by e.createDate desc ")
    List<CommentDto> listCommentByLesson( Long lessonId);

    @Query("select count(e.id) from Comment e" +
            " where  e.comment.id = :commentId ")
    Long countCommentByParentId( Long commentId);
}
