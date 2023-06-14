package com.goodjob.domain.likes.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikesRequestDto {
    private Long articleId;
    private Long commentId;
    private Long subCommentId;
}
