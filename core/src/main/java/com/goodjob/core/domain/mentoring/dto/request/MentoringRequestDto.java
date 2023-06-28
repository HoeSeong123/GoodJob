package com.goodjob.core.domain.mentoring.dto.request;

import com.goodjob.core.domain.hashTag.dto.response.HashTagResponseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MentoringRequestDto {
    @NotBlank(message="제목을 작성해주셔야 합니다.")
    @Size(max=30)
    private String title;
    @NotBlank(message="내용을 작성해주셔야 합니다.")
    private String content;
}
