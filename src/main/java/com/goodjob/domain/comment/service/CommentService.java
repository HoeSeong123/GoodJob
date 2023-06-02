package com.goodjob.domain.comment.service;

import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.comment.dto.request.CommentRequestDto;
import com.goodjob.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.comment.mapper.CommentMapper;
import com.goodjob.domain.comment.repository.CommentRepository;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.subComment.entity.SubComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public void createComment(Member member, Article article, CommentRequestDto commentRequestDto) {
        Comment comment = Comment
                .builder()
                .member(member)
                .article(article)
                .content(commentRequestDto.getContent())
                .isDeleted(false)
                .build();

        commentRepository.save(comment);
    }

    public CommentResponseDto getCommentResponseDto(Long id) {
        return commentMapper.toDto(commentRepository.findById(id).orElseThrow());

    }

    public void updateComment(Comment comment, String content) {
        comment.setContent(content);
        commentRepository.save(comment);
    }

    public Comment getComment(Long id) {
        return commentRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void deleteComment(Comment comment) {
        List<SubComment> subCommentList = comment.getSubCommentList();
        for(SubComment subComment : subCommentList) {
            subComment.setDeleted(true);
        }
        comment.setDeleted(true);
        commentRepository.save(comment);
    }
}
