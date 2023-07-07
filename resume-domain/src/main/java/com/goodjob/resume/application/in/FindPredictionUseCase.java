package com.goodjob.resume.application.in;



import com.goodjob.resume.dto.response.ResponsePredictionDto;

import java.util.List;

public interface FindPredictionUseCase {

    ResponsePredictionDto getPredictionByMemberId(Long memberId);

    ResponsePredictionDto getPredictionById(Long id);

    List<ResponsePredictionDto> getPredictions(Long memberId);
}
