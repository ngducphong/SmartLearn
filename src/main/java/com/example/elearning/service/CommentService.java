package com.example.elearning.service;


import com.example.elearning.dto.CommentDto;
import com.example.elearning.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    CommentDto saveComment(CommentDto dto) throws CustomException;
    CommentDto upDateComment(CommentDto dto, Long id) throws CustomException;

    void deleteComment(Long id) throws CustomException;

    List<CommentDto> getAllComment();

    CommentDto getCommentDtoById(Long id) throws CustomException;

    Page<CommentDto> pagingCommentDto(Pageable pageable);
    List<CommentDto> pagingCommentParent();
    List<CommentDto> pagingCommentChildrenByParentId(Long parentId);
    List<CommentDto> listCommentByLesson(Long lessonId);

}
