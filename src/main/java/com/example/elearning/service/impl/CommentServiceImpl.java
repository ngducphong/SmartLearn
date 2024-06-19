package com.example.elearning.service.impl;


import com.example.elearning.dto.CommentDto;
import com.example.elearning.exception.CustomException;
import com.example.elearning.model.Comment;
import com.example.elearning.model.Lesson;
import com.example.elearning.model.Users;
import com.example.elearning.repository.CommentRepository;
import com.example.elearning.repository.LessonRepository;
import com.example.elearning.repository.UserRepository;
import com.example.elearning.service.CommentService;
import com.example.elearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    UserService iUserService;

    public CommentDto save(Comment entity, CommentDto dto) throws CustomException {

        Users users = iUserService.getCurrentUser();
        if (users == null || users.getId() == null) {
            throw new CustomException("User not found");
        }
        entity.setUsers(users);
        entity.setVoided(dto.isVoided());
        entity.setContent(dto.getContent());

        if (dto.getLesson() == null || dto.getLesson().getId() == null) {
            throw new CustomException("Lesson is not null");
        }
        Lesson lesson = lessonRepository.findById(dto.getLesson().getId()).orElse(null);
        if (lesson == null) {
            throw new CustomException("Lesson not found");
        }
        entity.setLesson(lesson);

        if (dto.getComment() != null && dto.getComment().getId() != null) {
            Comment comment = commentRepository.findById(dto.getComment().getId()).orElse(null);
            entity.setComment(comment);
        }

        entity = commentRepository.save(entity);
        return new CommentDto(entity, true);
    }

    @Override
    public CommentDto saveComment(CommentDto dto) throws CustomException {
        Comment entity = new Comment();
        return this.save(entity, dto);
    }

    @Override
    public CommentDto upDateComment(CommentDto dto, Long id) throws CustomException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CustomException("Comment not found"));
        return this.save(comment, dto);
    }

    @Override
    public void deleteComment(Long id) throws CustomException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CustomException("Comment not found"));
        if(comment.getVoided() == null || comment.getVoided() == false){
            comment.setVoided(true);
        }else {
            comment.setVoided(false);
        }
        commentRepository.save(comment);
    }

    @Override
    public List<CommentDto> getAllComment() {
        return commentRepository.getAll();
    }

    @Override
    public CommentDto getCommentDtoById(Long id) throws CustomException {
        return new CommentDto(this.getCommentById(id));
    }

    private Comment getCommentById(Long id) throws CustomException {
        Optional<Comment> optional = commentRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new CustomException("Comment not found");
    }

    @Override
    public Page<CommentDto> pagingCommentDto(Pageable pageable) {
        Page<CommentDto> page = commentRepository.getCommentPage(pageable);
        return page;
    }

    @Override
    public List<CommentDto> pagingCommentParent() {
        List<CommentDto> list = commentRepository.getCommentParent();
        return list;
    }

    @Override
    public List<CommentDto> pagingCommentChildrenByParentId(Long parentId) {
        List<CommentDto> page = commentRepository.getCommentChildrenByParentId( parentId);
        return page;
    }

    @Override
    public List<CommentDto> listCommentByLesson(Long lessonId) {
        List<CommentDto> listComment = commentRepository.listCommentByLesson(lessonId).stream()
                .map(item -> {
                    Long totalComment = commentRepository.countCommentByParentId(item.getId());
                    item.setTotalCommentChild(totalComment); // Giả sử setTotalCommentChild thay đổi đối tượng hiện tại
                    return item;
                })
                .collect(Collectors.toList());
        return listComment;
    }

}
