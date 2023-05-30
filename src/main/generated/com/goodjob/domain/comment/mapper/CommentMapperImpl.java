package com.goodjob.domain.comment.mapper;

import com.goodjob.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.domain.comment.entity.Comment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-29T00:43:28+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentResponseDto toDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentResponseDto commentResponseDto = new CommentResponseDto();

        commentResponseDto.setId( comment.getId() );
        commentResponseDto.setCreateDate( comment.getCreateDate() );
        commentResponseDto.setModifiedDate( comment.getModifiedDate() );
        commentResponseDto.setMember( comment.getMember() );
        commentResponseDto.setArticle( comment.getArticle() );
        commentResponseDto.setContent( comment.getContent() );
        commentResponseDto.setLikeCount( comment.getLikeCount() );
        commentResponseDto.setDeleted( comment.isDeleted() );

        return commentResponseDto;
    }
}
